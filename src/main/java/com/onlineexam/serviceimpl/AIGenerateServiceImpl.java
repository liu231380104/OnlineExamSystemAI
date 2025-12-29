package com.onlineexam.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineexam.entity.FillQuestion;
import com.onlineexam.entity.JudgeQuestion;
import com.onlineexam.entity.MultiQuestion;
import com.onlineexam.service.AIGenerateService;
import com.onlineexam.service.FillQuestionService;
import com.onlineexam.service.JudgeQuestionService;
import com.onlineexam.service.MultiQuestionService;
import com.onlineexam.util.AIUtil;
import com.onlineexam.util.ApiResultHandler;
import com.onlineexam.vo.AIGenerateParam;
import com.onlineexam.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AIGenerateServiceImpl implements AIGenerateService {
    @Autowired
    private AIUtil aiUtil;

    @Autowired
    private MultiQuestionService multiQuestionService;

    @Autowired
    private FillQuestionService fillQuestionService;

    @Autowired
    private JudgeQuestionService judgeQuestionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiResult generateAndSave(AIGenerateParam param) {
        try {
            // 1. 构建提示词
            String prompt = buildPrompt(param);
            System.out.println("AI提示词: " + prompt);

            // 2. 调用AI生成题目
            String aiResponse = aiUtil.generateQuestions(prompt);
            System.out.println("AI返回内容: " + aiResponse);

            // 3. 解析题目
            List<?> questions = parseQuestions(aiResponse, param);
            System.out.println("解析到题目数量: " + questions.size());

            if (questions.isEmpty()) {
                return ApiResultHandler.buildApiResult(400, "生成失败: 无法解析AI返回的题目格式。AI返回内容: " + aiResponse.substring(0, Math.min(200, aiResponse.length())), null);
            }

            // 4. 保存题目
            int successCount = saveQuestions(questions, param);
            System.out.println("成功保存题目数量: " + successCount);

            if (successCount == 0) {
                return ApiResultHandler.buildApiResult(400, "生成失败: 题目解析成功但保存失败，请检查数据库连接", null);
            }

            return ApiResultHandler.buildApiResult(200, "生成成功",
                    "共生成并保存" + successCount + "道" + param.getSubject() + getQuestionTypeName(param.getQuestionType()) + "题");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResultHandler.buildApiResult(500, "生成失败: " + e.getMessage(), null);
        }
    }
    
    private String getQuestionTypeName(String type) {
        switch (type) {
            case "multi": return "选择题";
            case "fill": return "填空题";
            case "judge": return "判断题";
            default: return "题";
        }
    }

    // 构建提示词
    private String buildPrompt(AIGenerateParam param) {
        // 替换Java 8不支持的switch表达式为传统switch语句
        String typeDesc;
        String formatExample;
        switch (param.getQuestionType()) {
            case "multi":
                typeDesc = "选择题";
                formatExample = "格式示例：1. 题目内容 A.选项A B.选项B C.选项C D.选项D 【正确答案】\n" +
                               "注意：每题必须在一行内，选项用A. B. C. D.开头，正确答案用【】包裹，如【A】或【B】";
                break;
            case "fill":
                typeDesc = "填空题";
                formatExample = "格式示例：1. 题目内容，这里需要填写【答案内容】\n" +
                               "注意：每题必须在一行内，答案用【】包裹";
                break;
            case "judge":
                typeDesc = "判断题";
                formatExample = "格式示例：1. 题目内容 【对】或【错】\n" +
                               "注意：每题必须在一行内，答案用【对】或【错】表示";
                break;
            default:
                throw new IllegalArgumentException("未知题型");
        }

        // 构建基础提示词
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append(String.format("请生成%d道关于%s的%s", 
                param.getCount(), param.getSubject(), typeDesc));
        
        // 添加难度信息
        promptBuilder.append(String.format("，难度等级为%s", param.getLevel()));
        
        // 添加章节信息（如果有）
        if (param.getSection() != null && !param.getSection().trim().isEmpty()) {
            promptBuilder.append(String.format("，章节为%s", param.getSection()));
        }
        
        // 添加知识点信息（如果有）
        if (param.getKnowledgePoint() != null && !param.getKnowledgePoint().trim().isEmpty()) {
            promptBuilder.append(String.format("，重点考察知识点：%s", param.getKnowledgePoint()));
        }
        
        promptBuilder.append("。\n\n");
        promptBuilder.append("严格要求：\n");
        promptBuilder.append("1. 每题必须独立一行，不要换行\n");
        promptBuilder.append("2. 严格按照以下格式输出，不要添加任何额外说明或标记\n");
        promptBuilder.append("3. 只输出题目，不要输出其他内容\n");
        
        // 如果有知识点，强调要围绕知识点出题
        if (param.getKnowledgePoint() != null && !param.getKnowledgePoint().trim().isEmpty()) {
            promptBuilder.append(String.format("4. 题目必须围绕知识点\"%s\"进行设计，确保题目能够有效考察该知识点\n", 
                    param.getKnowledgePoint()));
        }
        
        promptBuilder.append("\n");
        promptBuilder.append(formatExample);
        promptBuilder.append("\n\n");
        promptBuilder.append(String.format("请直接输出%d道题目，每行一道：", param.getCount()));
        
        return promptBuilder.toString();
    }

    // 解析题目（根据题型适配）
    private List<?> parseQuestions(String response, AIGenerateParam param) {
        List<?> result = new ArrayList<>();
        String[] lines = response.split("\n");

        switch (param.getQuestionType()) {
            case "multi":
                result = parseMultiQuestions(lines, param);
                break;
            case "fill":
                result = parseFillQuestions(lines, param);
                break;
            case "judge":
                result = parseJudgeQuestions(lines, param);
                break;
        }
        return result;
    }

    // 解析选择题
    private List<MultiQuestion> parseMultiQuestions(String[] lines, AIGenerateParam param) {
        List<MultiQuestion> questions = new ArrayList<>();
        // 更灵活的正则表达式，支持多种格式
        // 模式1: 标准格式，使用前瞻断言确保选项内容匹配到下一个选项标记
        // 格式: 题目 A. 选项A B. 选项B C. 选项C D. 选项D 【答案】
        Pattern pattern1 = Pattern.compile("(.+?)A\\.\\s*([^B]*?)\\s+B\\.\\s*([^C]*?)\\s+C\\.\\s*([^D]*?)\\s+D\\.\\s*([^【]*?)\\s*【([A-D])】");
        // 模式2: 选项之间可能没有空格分隔符
        Pattern pattern2 = Pattern.compile("(.+?)A\\.([^B]*?)B\\.([^C]*?)C\\.([^D]*?)D\\.([^【]*?)【([A-D])】");
        // 模式3: 使用非贪婪匹配，允许选项内容中包含空格（备用）
        Pattern pattern3 = Pattern.compile("(.+?)A\\.\\s*(.+?)\\s+B\\.\\s*(.+?)\\s+C\\.\\s*(.+?)\\s+D\\.\\s*(.+?)\\s*【([A-D])】");
        // 模式4: 答案可能是完整单词，如"B选项"等，需要提取字母
        Pattern pattern4 = Pattern.compile("(.+?)A\\.\\s*([^B]*?)\\s+B\\.\\s*([^C]*?)\\s+C\\.\\s*([^D]*?)\\s+D\\.\\s*([^【]*?)\\s*【(.+?)】");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("格式") || line.startsWith("注意") || line.startsWith("请")) {
                continue; // 跳过说明文字
            }

            System.out.println("尝试解析选择题: " + line);
            
            Matcher matcher = null;
            boolean found = false;
            // 按顺序尝试匹配
            matcher = pattern1.matcher(line);
            if (matcher.find()) {
                found = true;
                System.out.println("使用模式1匹配成功");
            } else {
                matcher = pattern3.matcher(line);
                if (matcher.find()) {
                    found = true;
                    System.out.println("使用模式3匹配成功");
                } else {
                    matcher = pattern2.matcher(line);
                    if (matcher.find()) {
                        found = true;
                        System.out.println("使用模式2匹配成功");
                    } else {
                        matcher = pattern4.matcher(line);
                        if (matcher.find()) {
                            found = true;
                            System.out.println("使用模式4匹配成功");
                        } else {
                            System.out.println("所有模式都匹配失败，行内容: " + line);
                        }
                    }
                }
            }
            
            if (found && matcher != null) {
                try {
                    MultiQuestion q = new MultiQuestion();
                    q.setSubject(param.getSubject());
                    String question = matcher.group(1).replaceAll("^\\d+[.、]?\\s*", "").trim(); // 去除题号
                    q.setQuestion(question);
                    q.setAnswerA(matcher.group(2).trim());
                    q.setAnswerB(matcher.group(3).trim());
                    q.setAnswerC(matcher.group(4).trim());
                    q.setAnswerD(matcher.group(5).trim());
                    String rightAnswer = matcher.group(6).trim();
                    // 如果答案是"对"、"错"等，转换为A/B/C/D
                    if (rightAnswer.length() == 1 && "ABCD".contains(rightAnswer.toUpperCase())) {
                        q.setRightAnswer(rightAnswer.toUpperCase());
                    } else if (rightAnswer.length() > 1) {
                        // 尝试提取第一个字母
                        String firstChar = rightAnswer.substring(0, 1).toUpperCase();
                        if ("ABCD".contains(firstChar)) {
                            q.setRightAnswer(firstChar);
                        } else {
                            q.setRightAnswer("A"); // 默认值
                        }
                    } else {
                        q.setRightAnswer("A"); // 默认值
                    }
                    q.setLevel(param.getLevel());
                    q.setSection(param.getSection() == null ? "" : param.getSection());
                    q.setScore(2); // 默认分值
                    questions.add(q);
                } catch (Exception e) {
                    // 解析失败，跳过该题
                    System.err.println("解析选择题失败: " + line + ", 错误: " + e.getMessage());
                }
            }
        }
        return questions;
    }

    // 解析填空题
    private List<FillQuestion> parseFillQuestions(String[] lines, AIGenerateParam param) {
        List<FillQuestion> questions = new ArrayList<>();
        Pattern pattern = Pattern.compile("(.*?)【(.*?)】");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("格式") || line.startsWith("注意") || line.startsWith("请")) {
                continue; // 跳过说明文字
            }

            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                try {
                    FillQuestion q = new FillQuestion();
                    q.setSubject(param.getSubject());
                    String question = matcher.group(1).replaceAll("^\\d+[.、]?\\s*", "").trim(); // 去除题号
                    q.setQuestion(question);
                    q.setAnswer(matcher.group(2).trim());
                    q.setLevel(param.getLevel());
                    q.setSection(param.getSection() == null ? "" : param.getSection());
                    q.setScore(2);
                    questions.add(q);
                } catch (Exception e) {
                    // 解析失败，跳过该题
                    System.err.println("解析填空题失败: " + line + ", 错误: " + e.getMessage());
                }
            }
        }
        return questions;
    }

    // 解析判断题
    private List<JudgeQuestion> parseJudgeQuestions(String[] lines, AIGenerateParam param) {
        List<JudgeQuestion> questions = new ArrayList<>();
        Pattern pattern = Pattern.compile("(.*?)【(.*?)】");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("格式") || line.startsWith("注意") || line.startsWith("请")) {
                continue; // 跳过说明文字
            }

            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                try {
                    JudgeQuestion q = new JudgeQuestion();
                    q.setSubject(param.getSubject());
                    String question = matcher.group(1).replaceAll("^\\d+[.、]?\\s*", "").trim(); // 去除题号
                    q.setQuestion(question);
                    String answer = matcher.group(2).trim();
                    // 支持多种答案格式
                    if (answer.equals("对") || answer.equals("正确") || answer.equals("是") || answer.equals("true") || answer.equals("1")) {
                        q.setAnswer("1"); // 1=对
                    } else {
                        q.setAnswer("0"); // 0=错
                    }
                    q.setLevel(param.getLevel());
                    q.setSection(param.getSection() == null ? "" : param.getSection());
                    q.setScore(2);
                    questions.add(q);
                } catch (Exception e) {
                    // 解析失败，跳过该题
                    System.err.println("解析判断题失败: " + line + ", 错误: " + e.getMessage());
                }
            }
        }
        return questions;
    }

    // 保存题目
    private int saveQuestions(List<?> questions, AIGenerateParam param) {
        int count = 0;
        for (Object q : questions) {
            try {
                int result = 0;
                if (q instanceof MultiQuestion) {
                    result = multiQuestionService.add((MultiQuestion) q);
                } else if (q instanceof FillQuestion) {
                    result = fillQuestionService.add((FillQuestion) q);
                } else if (q instanceof JudgeQuestion) {
                    result = judgeQuestionService.add((JudgeQuestion) q);
                }
                if (result > 0) {
                    count++;
                } else {
                    System.err.println("保存题目失败，返回值为0");
                }
            } catch (Exception e) {
                // 记录单题保存失败，继续保存其他题目
                System.err.println("保存题目时发生异常: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return count;
    }

    @Override
    public ApiResult generatePreview(AIGenerateParam param) {
        try {
            // 1. 构建提示词
            String prompt = buildPrompt(param);
            System.out.println("AI提示词: " + prompt);

            // 2. 调用AI生成题目
            String aiResponse = aiUtil.generateQuestions(prompt);
            System.out.println("AI返回内容: " + aiResponse);

            // 3. 解析题目（不保存）
            List<?> questions = parseQuestions(aiResponse, param);
            System.out.println("解析到题目数量: " + questions.size());

            if (questions.isEmpty()) {
                return ApiResultHandler.buildApiResult(400, "生成失败: 无法解析AI返回的题目格式。AI返回内容: " + aiResponse.substring(0, Math.min(200, aiResponse.length())), null);
            }

            // 4. 返回题目列表（不保存）
            return ApiResultHandler.buildApiResult(200, "生成成功", questions);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResultHandler.buildApiResult(500, "生成失败: " + e.getMessage(), null);
        }
    }

    @Override
    public ApiResult batchSaveQuestions(String questionType, List<?> questions) {
        if (questions == null || questions.isEmpty()) {
            return ApiResultHandler.buildApiResult(400, "题目列表为空", null);
        }

        int successCount = 0;
        int failCount = 0;
        List<String> errorMessages = new ArrayList<>();

        for (Object q : questions) {
            try {
                int result = 0;
                
                // 将JSON对象转换为对应的实体类
                if ("multi".equals(questionType)) {
                    MultiQuestion multiQuestion;
                    if (q instanceof MultiQuestion) {
                        multiQuestion = (MultiQuestion) q;
                    } else if (q instanceof Map) {
                        // 从Map转换为MultiQuestion
                        multiQuestion = objectMapper.convertValue(q, MultiQuestion.class);
                    } else {
                        // 尝试通过JSON字符串转换
                        String json = objectMapper.writeValueAsString(q);
                        multiQuestion = objectMapper.readValue(json, MultiQuestion.class);
                    }
                    result = multiQuestionService.add(multiQuestion);
                } else if ("fill".equals(questionType)) {
                    FillQuestion fillQuestion;
                    if (q instanceof FillQuestion) {
                        fillQuestion = (FillQuestion) q;
                    } else if (q instanceof Map) {
                        fillQuestion = objectMapper.convertValue(q, FillQuestion.class);
                    } else {
                        String json = objectMapper.writeValueAsString(q);
                        fillQuestion = objectMapper.readValue(json, FillQuestion.class);
                    }
                    result = fillQuestionService.add(fillQuestion);
                } else if ("judge".equals(questionType)) {
                    JudgeQuestion judgeQuestion;
                    if (q instanceof JudgeQuestion) {
                        judgeQuestion = (JudgeQuestion) q;
                    } else if (q instanceof Map) {
                        judgeQuestion = objectMapper.convertValue(q, JudgeQuestion.class);
                    } else {
                        String json = objectMapper.writeValueAsString(q);
                        judgeQuestion = objectMapper.readValue(json, JudgeQuestion.class);
                    }
                    // 确保判断题的answer字段格式正确（1或0）
                    if (judgeQuestion.getAnswer() != null) {
                        String answer = judgeQuestion.getAnswer().trim();
                        if ("T".equalsIgnoreCase(answer) || "对".equals(answer) || "正确".equals(answer) || "true".equalsIgnoreCase(answer)) {
                            judgeQuestion.setAnswer("1");
                        } else if ("F".equalsIgnoreCase(answer) || "错".equals(answer) || "错误".equals(answer) || "false".equalsIgnoreCase(answer)) {
                            judgeQuestion.setAnswer("0");
                        }
                    }
                    result = judgeQuestionService.add(judgeQuestion);
                } else {
                    failCount++;
                    errorMessages.add("未知的题目类型: " + questionType);
                    continue;
                }

                if (result > 0) {
                    successCount++;
                } else {
                    failCount++;
                    errorMessages.add("保存失败，返回值为0");
                }
            } catch (Exception e) {
                failCount++;
                String errorMsg = "保存异常: " + e.getMessage();
                errorMessages.add(errorMsg);
                System.err.println(errorMsg);
                e.printStackTrace();
            }
        }

        String message = String.format("成功保存%d道题目", successCount);
        if (failCount > 0) {
            message += String.format("，失败%d道", failCount);
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errorMessages);
        
        return ApiResultHandler.buildApiResult(200, message, result);
    }
}
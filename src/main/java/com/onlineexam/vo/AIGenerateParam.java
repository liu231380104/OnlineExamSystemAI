package com.onlineexam.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AIGenerateParam {
    @NotBlank(message = "科目不能为空")
    private String subject; // 如"计算机网络"

    @NotBlank(message = "题型不能为空")
    private String questionType; // multi/fill/judge

    @NotNull(message = "数量不能为空")
    private Integer count; // 生成数量

    private String level = "中等"; // 难度

    private String section; // 章节

    private String knowledgePoint; // 考察知识点

    // getter/setter
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public String getKnowledgePoint() { return knowledgePoint; }
    public void setKnowledgePoint(String knowledgePoint) { this.knowledgePoint = knowledgePoint; }
}
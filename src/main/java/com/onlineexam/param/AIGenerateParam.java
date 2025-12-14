package com.onlineexam.param;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * AI生成题目请求参数类
 */
@Data
public class AIGenerateParam {
    // 科目（不能为空）
    @NotBlank(message = "科目名称不能为空")
    private String subject;

    // 题型（multi=选择题，fill=填空题，judge=判断题，不能为空）
    @NotBlank(message = "题型不能为空")
    private String questionType;

    // 生成数量（1-20之间，不能为空）
    @NotNull(message = "生成数量不能为空")
    @Min(value = 1, message = "生成数量至少1道")
    @Max(value = 20, message = "生成数量最多20道")
    private Integer count;

    // 难度等级（可选，默认3）
    private String level = "3";

    // 所属章节（可选）
    private String section = "";

    // 考察知识点（可选）
    private String knowledgePoint = "";
}
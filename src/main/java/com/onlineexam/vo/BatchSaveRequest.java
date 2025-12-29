package com.onlineexam.vo;

import java.util.List;

/**
 * 批量保存题目请求参数类
 */
public class BatchSaveRequest {
    // 题型（multi=选择题，fill=填空题，judge=判断题）
    private String questionType;
    
    // 题目列表
    private List<?> questions;

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<?> getQuestions() {
        return questions;
    }

    public void setQuestions(List<?> questions) {
        this.questions = questions;
    }
}


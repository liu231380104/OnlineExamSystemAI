package com.onlineexam.vo;

import lombok.Data;
import java.util.List;

/**
 * 批量保存题目请求参数类
 */
@Data
public class BatchSaveRequest {
    // 题型（multi=选择题，fill=填空题，judge=判断题）
    private String questionType;
    
    // 题目列表
    private List<?> questions;
}


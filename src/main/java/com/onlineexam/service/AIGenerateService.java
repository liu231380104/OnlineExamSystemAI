package com.onlineexam.service;

import com.onlineexam.entity.ApiResult;
import com.onlineexam.vo.AIGenerateParam;
import java.util.List;

public interface AIGenerateService {
    ApiResult generateAndSave(AIGenerateParam param);
    
    // 只生成不保存，返回题目列表供预览
    ApiResult<?> generatePreview(AIGenerateParam param);
    
    // 批量保存题目
    ApiResult<?> batchSaveQuestions(String questionType, List<?> questions);
}
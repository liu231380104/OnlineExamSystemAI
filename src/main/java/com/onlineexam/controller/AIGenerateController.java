package com.onlineexam.controller;

import com.onlineexam.entity.ApiResult;
import com.onlineexam.service.AIGenerateService;
import com.onlineexam.vo.AIGenerateParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIGenerateController {

    @Autowired
    private AIGenerateService aiGenerateService;

    @PostMapping("/ai/generate")
    public ApiResult generate(@RequestBody AIGenerateParam param) {
        return aiGenerateService.generateAndSave(param);
    }

    @PostMapping("/ai/generate/preview")
    public ApiResult generatePreview(@RequestBody AIGenerateParam param) {
        return aiGenerateService.generatePreview(param);
    }

    @PostMapping("/ai/batchSave")
    public ApiResult batchSave(@RequestBody com.onlineexam.vo.BatchSaveRequest request) {
        return aiGenerateService.batchSaveQuestions(request.getQuestionType(), request.getQuestions());
    }
}
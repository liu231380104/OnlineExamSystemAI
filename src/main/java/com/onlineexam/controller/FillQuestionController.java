package com.onlineexam.controller;

import com.onlineexam.entity.ApiResult;
import com.onlineexam.entity.FillQuestion;
import com.onlineexam.serviceimpl.FillQuestionServiceImpl;
import com.onlineexam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FillQuestionController {

    @Autowired
    private FillQuestionServiceImpl fillQuestionService;

    @PostMapping("/fillQuestion")
    public ApiResult add(@RequestBody FillQuestion fillQuestion) {
        int res = fillQuestionService.add(fillQuestion);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200,"添加成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"添加失败",res);
    }

    @GetMapping("/fillQuestionId")
    public ApiResult findOnlyQuestionId() {
        FillQuestion res = fillQuestionService.findOnlyQuestionId();
        return ApiResultHandler.buildApiResult(200,"查询成功",res);
    }

    @PostMapping("/editFillQuestion")
    public ApiResult edit(@RequestBody FillQuestion fillQuestion) {
        int res = fillQuestionService.edit(fillQuestion);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200,"修改成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"修改失败",res);
    }

    @GetMapping("/fillQuestions/{page}/{size}/{subject}/{question}")
    public ApiResult list(@org.springframework.web.bind.annotation.PathVariable Integer page,
                          @org.springframework.web.bind.annotation.PathVariable Integer size,
                          @org.springframework.web.bind.annotation.PathVariable String subject,
                          @org.springframework.web.bind.annotation.PathVariable String question) {
        return ApiResultHandler.buildApiResult(200, "查询成功", fillQuestionService.findAll(page, size));
    }
}

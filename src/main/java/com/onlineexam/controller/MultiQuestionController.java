package com.onlineexam.controller;

import com.onlineexam.entity.ApiResult;
import com.onlineexam.entity.MultiQuestion;
import com.onlineexam.serviceimpl.MultiQuestionServiceImpl;
import com.onlineexam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MultiQuestionController {

    @Autowired
    private MultiQuestionServiceImpl multiQuestionService;

    @GetMapping("/multiQuestionId")
    public ApiResult findOnlyQuestion() {
        MultiQuestion res = multiQuestionService.findOnlyQuestionId();
        return ApiResultHandler.buildApiResult(200,"查询成功",res);
    }

    @PostMapping("/MultiQuestion")
    public ApiResult add(@RequestBody MultiQuestion multiQuestion) {
        int res = multiQuestionService.add(multiQuestion);
        if (res != 0) {

            return ApiResultHandler.buildApiResult(200,"添加成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"添加失败",res);
    }

    @PostMapping("/editMultiQuestion")
    public ApiResult edit(@RequestBody MultiQuestion multiQuestion) {
        int res = multiQuestionService.edit(multiQuestion);
        if (res != 0) {

            return ApiResultHandler.buildApiResult(200,"修改成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"修改失败",res);
    }

    @GetMapping("/choiceQuestions/{page}/{size}/{subject}/{question}")
    public ApiResult list(@org.springframework.web.bind.annotation.PathVariable Integer page,
                          @org.springframework.web.bind.annotation.PathVariable Integer size,
                          @org.springframework.web.bind.annotation.PathVariable String subject,
                          @org.springframework.web.bind.annotation.PathVariable String question) {
        // 前端使用 @ 作为占位，服务端直接忽略过滤，返回分页列表
        return ApiResultHandler.buildApiResult(200, "查询成功", multiQuestionService.findAll(page, size));
    }
}

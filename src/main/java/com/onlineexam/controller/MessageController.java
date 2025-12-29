package com.onlineexam.controller;

import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.ApiResult;
import com.onlineexam.entity.Message;
import com.onlineexam.service.MessageService;
import com.onlineexam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/messages/{page}/{size}")
    public ApiResult findAll(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        PageInfo<Message> all = messageService.findAll(page, size);
        return ApiResultHandler.buildApiResult(200,"查询所有留言",all);
    }

    @GetMapping("/message/{id}")
    public ApiResult findById(@PathVariable("id") Integer id) {
        Message res = messageService.findById(id);
        return ApiResultHandler.buildApiResult(200,"根据Id查询",res);
    }

    @DeleteMapping("/message/{id}")
    public int delete(@PathVariable("id") Integer id) {
        int res = messageService.delete(id);
        return res;
    }

    @PostMapping("/message")
    public ApiResult add(@RequestBody Message message) {
        int res = messageService.add(message);
        if (res == 0) {
            return ApiResultHandler.buildApiResult(400,"添加失败",res);
        } else {
            return ApiResultHandler.buildApiResult(200,"添加成功",res);
        }
    }
}

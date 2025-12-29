package com.onlineexam.service;

import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.Message;

public interface MessageService {
    PageInfo<Message> findAll(Integer pageNum, Integer pageSize);

    Message findById(Integer id);

    int delete(Integer id);

    int update(Message message);

    int add(Message message);
}

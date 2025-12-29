package com.onlineexam.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.Message;
import com.onlineexam.mapper.MessageMapper;
import com.onlineexam.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public PageInfo<Message> findAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Message> messages = messageMapper.findAll();
        return new PageInfo<>(messages);
    }

    @Override
    public Message findById(Integer id) {
        return messageMapper.findById(id);
    }

    @Override
    public int delete(Integer id) {
        return messageMapper.delete(id);
    }

    @Override
    public int update(Message message) {
        return messageMapper.update(message);
    }

    @Override
    public int add(Message message) {
        return messageMapper.add(message);
    }
}

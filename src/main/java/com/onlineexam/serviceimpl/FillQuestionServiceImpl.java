package com.onlineexam.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.FillQuestion;
import com.onlineexam.mapper.FillQuestionMapper;
import com.onlineexam.service.FillQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FillQuestionServiceImpl implements FillQuestionService {

    @Autowired
    private FillQuestionMapper fillQuestionMapper;

    @Override
    public List<FillQuestion> findByIdAndType(Integer paperId) {
        return fillQuestionMapper.findByIdAndType(paperId);
    }

    @Override
    public PageInfo<FillQuestion> findAll(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<FillQuestion> fillQuestions = fillQuestionMapper.findAll();
        return new PageInfo<>(fillQuestions);
    }

    @Override
    public FillQuestion findOnlyQuestionId() {
        return fillQuestionMapper.findOnlyQuestionId();
    }

    @Override
    public int add(FillQuestion fillQuestion) {
        return fillQuestionMapper.add(fillQuestion);
    }

    @Override
    public List<Integer> findBySubject(String subject, Integer pageNo) {
        return fillQuestionMapper.findBySubject(subject,pageNo);
    }

    @Override
    public int edit(FillQuestion fillQuestion) {
        return fillQuestionMapper.edit(fillQuestion);
    }

    @Override
    public List<FillQuestion> findBySubject(String subject) {
        return fillQuestionMapper.findQuestionBySubject(subject);
    }
}

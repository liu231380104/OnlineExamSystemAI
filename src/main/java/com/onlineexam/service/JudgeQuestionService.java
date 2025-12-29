package com.onlineexam.service;

import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.JudgeQuestion;

import java.util.List;

public interface JudgeQuestionService {

    List<JudgeQuestion> findByIdAndType(Integer paperId);

    PageInfo<JudgeQuestion> findAll(Integer page, Integer size);

    JudgeQuestion findOnlyQuestionId();

    int add(JudgeQuestion judgeQuestion);

    List<Integer> findBySubject(String subject,Integer pageNo);

    int edit(JudgeQuestion judgeQuestion);

    List<JudgeQuestion> findBySubject(String subject);

}

package com.onlineexam.service;

import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.FillQuestion;

import java.util.List;

public interface FillQuestionService {

    List<FillQuestion> findByIdAndType(Integer paperId);

    PageInfo<FillQuestion> findAll(Integer page, Integer size);

    FillQuestion findOnlyQuestionId();

    int add(FillQuestion fillQuestion);

    List<Integer> findBySubject(String subject,Integer pageNo);

    int edit(FillQuestion fillQuestion);

    List<FillQuestion> findBySubject(String subject);
}

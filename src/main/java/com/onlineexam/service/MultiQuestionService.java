package com.onlineexam.service;

import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.MultiQuestion;

import java.util.List;

public interface MultiQuestionService {

    List<MultiQuestion> findByIdAndType(Integer PaperId);

    PageInfo<MultiQuestion> findAll(Integer page, Integer size);

    MultiQuestion findOnlyQuestionId();

    int add(MultiQuestion multiQuestion);

    List<Integer> findBySubject(String subject,Integer pageNo);

    int edit(MultiQuestion multiQuestion);

    List<MultiQuestion> findBySubject(String subject);
}

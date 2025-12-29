package com.onlineexam.service;

import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.Score;

import java.util.List;

public interface ScoreService {
    int add(Score score);

    List<Score> findAll();

    PageInfo<Score> findById(Integer studentId, Integer pageNum, Integer pageSize);

    List<Score> findByExamCode(Integer examCode);
}

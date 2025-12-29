package com.onlineexam.service;

import com.github.pagehelper.PageInfo;
import com.onlineexam.vo.AnswerVO;
import com.onlineexam.vo.QuestionVO;

public interface AnswerService {

    PageInfo<AnswerVO> findAll(Integer page, Integer size, String subject, String section, String question);

    /**
     * 根据类型和id获取题目
     *
     * @param type 类型
     * @param questionId 题目id
     * @return 题目信息
     */
    QuestionVO findByIdAndType(String type, Long questionId);
}

package com.onlineexam.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlineexam.mapper.AnswerMapper;
import com.onlineexam.service.AnswerService;
import com.onlineexam.vo.AnswerVO;
import com.onlineexam.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public IPage<AnswerVO> findAll(Page<AnswerVO> page, String subject, String section, String question) {
        subject = (subject.equals("@") ? "" : subject);
        section = (section.equals("@") ? "" : section);
        question = (question.equals("@") ? "" : question);
        return answerMapper.findAll(page, subject, section, question);
    }

    @Override
    public QuestionVO findByIdAndType(String type, Long questionId) {
        QuestionVO questionVO = new QuestionVO();
        questionVO.setType(type);
        switch (type) {
            case "选择题":
                questionVO.setMultiQuestion(answerMapper.findMultiQuestionById(questionId));
                break;
            case "判断题":
                questionVO.setJudgeQuestion(answerMapper.findJudgeQuestionById(questionId));
                break;
            case "填空题":
                questionVO.setFillQuestion(answerMapper.findFillQuestionById(questionId));
                break;
        }
        return questionVO;
    }
}

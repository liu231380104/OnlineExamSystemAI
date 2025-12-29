package com.onlineexam.mapper;

import com.onlineexam.entity.FillQuestion;
import com.onlineexam.entity.JudgeQuestion;
import com.onlineexam.entity.MultiQuestion;
import com.onlineexam.vo.AnswerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface AnswerMapper {
    @Select("select questionId, question, subject, score, section,level, \"选择题\" as type from multi_question where question like concat('%',#{question},'%') and subject like concat('%',#{subject},'%') and section like concat('%',#{section},'%')" +
            "union select questionId,question, subject, score, section,level, \"判断题\" as type  from judge_question where question like concat('%',#{question},'%') and subject like concat('%',#{subject},'%') and section like concat('%',#{section},'%')" +
            "union select questionId,question, subject, score, section,level, \"填空题\" as type from fill_question where question like  concat('%',#{question},'%') and subject like concat('%',#{subject},'%') and section like concat('%',#{section},'%')")
    List<AnswerVO> findAll(@Param("subject") String subject, @Param("section") String section, @Param("question") String question);


    /**
     * 查选择题
     * @param questionId 选择题id
     * @return 选择题
     */
    @Select("select questionId, subject, question, answerA, answerB, answerC, answerD, rightAnswer, section, level, analysis from multi_question where questionId = #{questionId}")
    MultiQuestion findMultiQuestionById(Long questionId);

    /**
     * 查填空题
     *
     * @param questionId 题目id
     * @return 填空题
     */
    @Select("select questionId, subject, question, answer, analysis, level, section from fill_question where questionId = #{questionId}")
    FillQuestion findFillQuestionById(Long questionId);

    /**
     * 查判断题
     *
     * @param questionId 题目id
     * @return 判断题
     */
    @Select("select questionId, subject, question, answer, analysis, level, section from judge_question where questionId = #{questionId}")
    JudgeQuestion findJudgeQuestionById(Long questionId);
}

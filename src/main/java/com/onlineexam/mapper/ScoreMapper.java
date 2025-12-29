package com.onlineexam.mapper;

import com.onlineexam.entity.Score;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ScoreMapper {
    /**
     * @param score 添加一条成绩记录
     * @return
     */
    @Options(useGeneratedKeys = true,keyProperty = "scoreId")
    @Insert("insert into score(examCode,studentId,subject,ptScore,etScore,score,answerDate) values(#{examCode},#{studentId},#{subject},#{ptScore},#{etScore},#{score},#{answerDate})")
    int add(Score score);

    @Select("select scoreId,examCode,studentId,subject,ptScore,etScore,score,answerDate from score order by scoreId desc")
    List<Score> findAll();

    // 分页 - 使用PageHelper实现
    @Select("select scoreId,examCode,studentId,subject,ptScore,etScore,score,answerDate from score where studentId = #{studentId} order by scoreId asc")
    List<Score> findById(@Param("studentId") Integer studentId);

    /**
     *
     * @return 查询学生的学科分数。
     */
    @Select("select * from score where examCode = #{examCode}")
    List<Score> findByExamCode(Integer examCode);
}

package com.onlineexam.mapper;

import com.onlineexam.entity.ExamVideo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExamVideoMapper {
    /**
     * 添加一条视频记录
     * @param examVideo 视频信息
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "videoId")
    @Insert("insert into exam_video(examCode,studentId,videoPath,videoName,captureTime,duration,uploadTime) " +
            "values(#{examCode},#{studentId},#{videoPath},#{videoName},#{captureTime},#{duration},#{uploadTime})")
    int add(ExamVideo examVideo);

    /**
     * 根据考试编号和学生ID查询视频列表
     * @param examCode 考试编号
     * @param studentId 学生ID
     * @return
     */
    @Select("select videoId,examCode,studentId,videoPath,videoName,captureTime,duration,uploadTime " +
            "from exam_video where examCode = #{examCode} and studentId = #{studentId} " +
            "order by captureTime desc")
    List<ExamVideo> findByExamCodeAndStudentId(@Param("examCode") Integer examCode, @Param("studentId") Integer studentId);

    /**
     * 根据考试编号查询所有学生的视频
     * @param examCode 考试编号
     * @return
     */
    @Select("select videoId,examCode,studentId,videoPath,videoName,captureTime,duration,uploadTime " +
            "from exam_video where examCode = #{examCode} order by studentId, captureTime desc")
    List<ExamVideo> findByExamCode(@Param("examCode") Integer examCode);

    /**
     * 根据学生ID查询所有视频
     * @param studentId 学生ID
     * @return
     */
    @Select("select videoId,examCode,studentId,videoPath,videoName,captureTime,duration,uploadTime " +
            "from exam_video where studentId = #{studentId} order by captureTime desc")
    List<ExamVideo> findByStudentId(@Param("studentId") Integer studentId);

    /**
     * 根据视频ID查询视频信息
     * @param videoId 视频ID
     * @return
     */
    @Select("select videoId,examCode,studentId,videoPath,videoName,captureTime,duration,uploadTime " +
            "from exam_video where videoId = #{videoId}")
    ExamVideo findByVideoId(@Param("videoId") Integer videoId);
}


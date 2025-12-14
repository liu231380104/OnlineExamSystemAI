package com.onlineexam.service;

import com.onlineexam.entity.ExamVideo;

import java.util.List;

public interface ExamVideoService {
    /**
     * 添加视频记录
     * @param examVideo 视频信息
     * @return
     */
    int add(ExamVideo examVideo);

    /**
     * 根据考试编号和学生ID查询视频列表
     * @param examCode 考试编号
     * @param studentId 学生ID
     * @return
     */
    List<ExamVideo> findByExamCodeAndStudentId(Integer examCode, Integer studentId);

    /**
     * 根据考试编号查询所有学生的视频
     * @param examCode 考试编号
     * @return
     */
    List<ExamVideo> findByExamCode(Integer examCode);

    /**
     * 根据学生ID查询所有视频
     * @param studentId 学生ID
     * @return
     */
    List<ExamVideo> findByStudentId(Integer studentId);

    /**
     * 根据视频ID查询视频信息
     * @param videoId 视频ID
     * @return
     */
    ExamVideo findByVideoId(Integer videoId);
}


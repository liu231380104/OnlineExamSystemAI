package com.onlineexam.service;

import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.ExamManage;

import java.util.List;

public interface ExamManageService {

    /**
     * 不分页查询所有考试信息
     */
    List<ExamManage> findAll();
    PageInfo<ExamManage> findAll(Integer page, Integer size);
    PageInfo<ExamManage> findByTeacher(Integer page, Integer size, Integer teacherId, String teacherInstitute);
    PageInfo<ExamManage> findByStudentId(Integer page, Integer size, Integer studentId);
    PageInfo<ExamManage> findByTeacherCourses(Integer page, Integer size, Integer teacherId, String teacherInstitute);

    ExamManage findById(Integer examCode);

    int delete(Integer examCode);

    int update(ExamManage exammanage);

    int add(ExamManage exammanage);

    ExamManage findOnlyPaperId();


}

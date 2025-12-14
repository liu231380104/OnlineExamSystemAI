package com.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlineexam.entity.ExamManage;

import java.util.List;

public interface ExamManageService {

    /**
     * 不分页查询所有考试信息
     */
    List<ExamManage> findAll();
    IPage<ExamManage> findAll(Page<ExamManage> page);
    IPage<ExamManage> findByTeacher(Page<ExamManage> page, Integer teacherId, String teacherInstitute);
    IPage<ExamManage> findByStudentId(Page<ExamManage> page, Integer studentId);
    IPage<ExamManage> findByTeacherCourses(Page<ExamManage> page, Integer teacherId, String teacherInstitute);

    ExamManage findById(Integer examCode);

    int delete(Integer examCode);

    int update(ExamManage exammanage);

    int add(ExamManage exammanage);

    ExamManage findOnlyPaperId();


}

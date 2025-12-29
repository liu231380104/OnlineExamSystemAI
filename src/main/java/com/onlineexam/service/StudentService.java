package com.onlineexam.service;

import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.Student;

public interface StudentService {

    PageInfo<Student> findAll(Integer page, Integer size, String name, String grade,
                           String tel, String institute, String major, String clazz, String teacherInstitute);

    Student findById(Integer studentId);

    int deleteById(Integer studentId);

    int update(Student student);

    int updatePwd(Student student);
    int add(Student student);

    /**
     * 按教师授课关系查询学生（不限制学院）
     */
    PageInfo<Student> findByTeacher(Integer page, Integer size, Integer teacherId,
                                    String name, String grade, String tel,
                                    String institute, String major, String clazz);
}

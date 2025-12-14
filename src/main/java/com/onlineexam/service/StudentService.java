package com.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlineexam.entity.Student;

public interface StudentService {

    IPage<Student> findAll(Page<Student> page, String name, String grade,
                           String tel, String institute, String major, String clazz, String teacherInstitute);

    Student findById(Integer studentId);

    int deleteById(Integer studentId);

    int update(Student student);

    int updatePwd(Student student);
    int add(Student student);
}

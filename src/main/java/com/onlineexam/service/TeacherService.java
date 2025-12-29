package com.onlineexam.service;

import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.Teacher;

import java.util.List;

public interface TeacherService {

    PageInfo<Teacher> findAll(Integer current, Integer size);

    public List<Teacher> findAll();

    public Teacher findById(Integer teacherId);

    public int deleteById(Integer teacherId);

    public int update(Teacher teacher);

    public int add(Teacher teacher);
}

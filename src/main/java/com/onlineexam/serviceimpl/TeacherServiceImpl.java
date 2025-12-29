package com.onlineexam.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.Teacher;
import com.onlineexam.mapper.TeacherMapper;
import com.onlineexam.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public PageInfo<Teacher> findAll(Integer current, Integer size) {
        PageHelper.startPage(current, size);
        List<Teacher> teachers = teacherMapper.findAll();
        return new PageInfo<>(teachers);
    }

    @Override
    public List<Teacher> findAll() {
        return teacherMapper.findAll();
    }

    @Override
    public Teacher findById(Integer teacherId) {
        return teacherMapper.findById(teacherId);
    }

    @Override
    public int deleteById(Integer teacherId) {
        return teacherMapper.deleteById(teacherId);
    }

    @Override
    public int update(Teacher teacher) {
        return teacherMapper.update(teacher);
    }

    @Override
    public int add(Teacher teacher) {
        teacher.setRole("1");
        return teacherMapper.add(teacher);
    }
}

package com.onlineexam.serviceimpl;

import com.onlineexam.entity.Course;
import com.onlineexam.mapper.CourseMapper;
import com.onlineexam.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> findAll() {
        return courseMapper.findAll();
    }

    @Override
    public Course findById(Integer courseId) {
        return courseMapper.findById(courseId);
    }

    @Override
    public Course findByCode(String courseCode) {
        return courseMapper.findByCode(courseCode);
    }

    @Override
    public int add(Course course) {
        return courseMapper.add(course);
    }

    @Override
    public int update(Course course) {
        return courseMapper.update(course);
    }

    @Override
    public int delete(Integer courseId) {
        return courseMapper.delete(courseId);
    }
}


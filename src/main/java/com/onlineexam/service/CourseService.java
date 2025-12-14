package com.onlineexam.service;

import com.onlineexam.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> findAll();
    Course findById(Integer courseId);
    Course findByCode(String courseCode);
    int add(Course course);
    int update(Course course);
    int delete(Integer courseId);
}


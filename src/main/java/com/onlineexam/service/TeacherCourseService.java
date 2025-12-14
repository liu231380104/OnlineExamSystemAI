package com.onlineexam.service;

import com.onlineexam.entity.Course;
import com.onlineexam.entity.TeacherCourse;

import java.util.List;

public interface TeacherCourseService {
    int add(TeacherCourse teacherCourse);
    int delete(Integer teacherId, Integer courseId);
    List<Course> findCoursesByTeacherId(Integer teacherId);
    List<TeacherCourse> findTeachersByCourseId(Integer courseId);
    int checkTeacherCourse(Integer teacherId, Integer courseId);
}


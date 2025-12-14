package com.onlineexam.service;

import com.onlineexam.entity.Course;
import com.onlineexam.entity.StudentCourse;

import java.util.List;

public interface StudentCourseService {
    int add(StudentCourse studentCourse);
    int delete(Integer studentId, Integer courseId);
    List<Course> findCoursesByStudentId(Integer studentId);
    StudentCourse findByStudentAndCourse(Integer studentId, Integer courseId);
    List<StudentCourse> findStudentsByCourseAndTeacher(Integer courseId, Integer teacherId);
    int checkStudentCourse(Integer studentId, Integer courseId);
    Integer getTeacherIdByStudentAndCourse(Integer studentId, Integer courseId);
}


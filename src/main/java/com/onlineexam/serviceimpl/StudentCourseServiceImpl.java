package com.onlineexam.serviceimpl;

import com.onlineexam.entity.Course;
import com.onlineexam.entity.StudentCourse;
import com.onlineexam.mapper.StudentCourseMapper;
import com.onlineexam.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {
    @Autowired
    private StudentCourseMapper studentCourseMapper;

    @Override
    public int add(StudentCourse studentCourse) {
        return studentCourseMapper.add(studentCourse);
    }

    @Override
    public int delete(Integer studentId, Integer courseId) {
        return studentCourseMapper.delete(studentId, courseId);
    }

    @Override
    public List<Course> findCoursesByStudentId(Integer studentId) {
        return studentCourseMapper.findCoursesByStudentId(studentId);
    }

    @Override
    public StudentCourse findByStudentAndCourse(Integer studentId, Integer courseId) {
        return studentCourseMapper.findByStudentAndCourse(studentId, courseId);
    }

    @Override
    public List<StudentCourse> findStudentsByCourseAndTeacher(Integer courseId, Integer teacherId) {
        return studentCourseMapper.findStudentsByCourseAndTeacher(courseId, teacherId);
    }

    @Override
    public int checkStudentCourse(Integer studentId, Integer courseId) {
        return studentCourseMapper.checkStudentCourse(studentId, courseId);
    }

    @Override
    public Integer getTeacherIdByStudentAndCourse(Integer studentId, Integer courseId) {
        return studentCourseMapper.getTeacherIdByStudentAndCourse(studentId, courseId);
    }
}


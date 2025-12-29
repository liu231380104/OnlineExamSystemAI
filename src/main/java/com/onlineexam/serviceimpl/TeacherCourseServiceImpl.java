package com.onlineexam.serviceimpl;

import com.onlineexam.entity.Course;
import com.onlineexam.entity.TeacherCourse;
import com.onlineexam.mapper.TeacherCourseMapper;
import com.onlineexam.service.TeacherCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherCourseServiceImpl implements TeacherCourseService {
    @Autowired
    private TeacherCourseMapper teacherCourseMapper;

    @Override
    public int add(TeacherCourse teacherCourse) {
        return teacherCourseMapper.add(teacherCourse);
    }

    @Override
    public int delete(Integer teacherId, Integer courseId) {
        return teacherCourseMapper.delete(teacherId, courseId);
    }

    @Override
    public List<Course> findCoursesByTeacherId(Integer teacherId) {
        return teacherCourseMapper.findCoursesByTeacherId(teacherId);
    }

    @Override
    public List<TeacherCourse> findTeachersByCourseId(Integer courseId) {
        return teacherCourseMapper.findTeachersByCourseId(courseId);
    }

    @Override
    public int deleteAllByTeacherId(Integer teacherId) {
        return teacherCourseMapper.deleteAllByTeacherId(teacherId);
    }

    @Override
    public int checkTeacherCourse(Integer teacherId, Integer courseId) {
        return teacherCourseMapper.checkTeacherCourse(teacherId, courseId);
    }
}

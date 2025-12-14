package com.onlineexam.vo;

import com.onlineexam.entity.Course;
import com.onlineexam.entity.Teacher;

import java.util.List;

/**
 * 课程及其教师信息VO
 */
public class CourseWithTeachersVO {
    private Course course;
    private List<Teacher> teachers;

    public CourseWithTeachersVO() {
    }

    public CourseWithTeachersVO(Course course, List<Teacher> teachers) {
        this.course = course;
        this.teachers = teachers;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
}


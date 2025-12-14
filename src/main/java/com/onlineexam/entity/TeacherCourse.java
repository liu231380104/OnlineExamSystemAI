package com.onlineexam.entity;

/**
 * 教师课程关联实体类
 */
public class TeacherCourse {
    private Integer id;
    private Integer teacherId;
    private Integer courseId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "TeacherCourse{" +
                "id=" + id +
                ", teacherId=" + teacherId +
                ", courseId=" + courseId +
                '}';
    }
}


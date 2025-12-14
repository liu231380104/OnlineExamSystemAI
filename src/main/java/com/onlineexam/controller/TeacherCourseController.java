package com.onlineexam.controller;

import com.onlineexam.entity.ApiResult;
import com.onlineexam.entity.Course;
import com.onlineexam.entity.Teacher;
import com.onlineexam.entity.TeacherCourse;
import com.onlineexam.serviceimpl.TeacherCourseServiceImpl;
import com.onlineexam.serviceimpl.TeacherServiceImpl;
import com.onlineexam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class TeacherCourseController {
    @Autowired
    private TeacherCourseServiceImpl teacherCourseService;

    @Autowired
    private TeacherServiceImpl teacherService;

    /**
     * 教师添加教授的课程
     */
    @PostMapping("/teacher/course")
    public ApiResult add(@RequestBody TeacherCourse teacherCourse, HttpServletRequest request) {
        // 获取当前登录教师ID
        Integer teacherId = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("rb_teacher_id".equals(cookie.getName())) {
                    try {
                        teacherId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        // 忽略
                    }
                }
            }
        }
        
        // 如果是教师，自动设置teacherId
        if (teacherId != null) {
            teacherCourse.setTeacherId(teacherId);
        }
        
        int res = teacherCourseService.add(teacherCourse);
        if (res > 0) {
            return ApiResultHandler.buildApiResult(200, "添加成功", teacherCourse);
        } else {
            return ApiResultHandler.buildApiResult(400, "添加失败，可能已存在该关联", null);
        }
    }

    /**
     * 教师删除教授的课程
     */
    @DeleteMapping("/teacher/course/{courseId}")
    public ApiResult delete(@PathVariable Integer courseId, HttpServletRequest request) {
        // 获取当前登录教师ID
        Integer teacherId = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("rb_teacher_id".equals(cookie.getName())) {
                    try {
                        teacherId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        // 忽略
                    }
                }
            }
        }
        
        if (teacherId == null) {
            return ApiResultHandler.buildApiResult(400, "未登录或不是教师", null);
        }
        
        int res = teacherCourseService.delete(teacherId, courseId);
        if (res > 0) {
            return ApiResultHandler.buildApiResult(200, "删除成功", null);
        } else {
            return ApiResultHandler.buildApiResult(400, "删除失败", null);
        }
    }

    /**
     * 查询教师教授的课程列表
     */
    @GetMapping("/teacher/courses")
    public ApiResult findCoursesByTeacher(HttpServletRequest request) {
        // 获取当前登录教师ID
        Integer teacherId = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("rb_teacher_id".equals(cookie.getName())) {
                    try {
                        teacherId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        // 忽略
                    }
                }
            }
        }
        
        if (teacherId == null) {
            return ApiResultHandler.buildApiResult(400, "未登录或不是教师", null);
        }
        
        List<Course> courses = teacherCourseService.findCoursesByTeacherId(teacherId);
        return ApiResultHandler.buildApiResult(200, "请求成功", courses);
    }

    /**
     * 根据课程ID查询教授该课程的教师列表（用于学生选课）
     */
    @GetMapping("/course/{courseId}/teachers")
    public ApiResult findTeachersByCourseId(@PathVariable Integer courseId) {
        List<TeacherCourse> teacherCourses = teacherCourseService.findTeachersByCourseId(courseId);
        // 获取教师详细信息
        java.util.List<Teacher> teachers = new java.util.ArrayList<>();
        for (TeacherCourse tc : teacherCourses) {
            Teacher teacher = teacherService.findById(tc.getTeacherId());
            if (teacher != null) {
                teachers.add(teacher);
            }
        }
        return ApiResultHandler.buildApiResult(200, "请求成功", teachers);
    }
}


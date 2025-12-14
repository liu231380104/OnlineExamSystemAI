package com.onlineexam.controller;

import com.onlineexam.entity.ApiResult;
import com.onlineexam.entity.Course;
import com.onlineexam.entity.StudentCourse;
import com.onlineexam.serviceimpl.StudentCourseServiceImpl;
import com.onlineexam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class StudentCourseController {
    @Autowired
    private StudentCourseServiceImpl studentCourseService;

    /**
     * 学生选课
     */
    @PostMapping("/student/course")
    public ApiResult add(@RequestBody StudentCourse studentCourse, HttpServletRequest request) {
        // 获取当前登录学生ID
        Integer studentId = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("rb_student_id".equals(cookie.getName())) {
                    try {
                        studentId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        // 忽略
                    }
                }
            }
        }
        
        if (studentId == null) {
            return ApiResultHandler.buildApiResult(400, "未登录或不是学生", null);
        }
        
        // 检查是否已选该课程
        int count = studentCourseService.checkStudentCourse(studentId, studentCourse.getCourseId());
        if (count > 0) {
            return ApiResultHandler.buildApiResult(400, "您已选择该课程", null);
        }
        
        // 自动设置studentId和选课时间
        studentCourse.setStudentId(studentId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        studentCourse.setSelectTime(sdf.format(new Date()));
        
        int res = studentCourseService.add(studentCourse);
        if (res > 0) {
            return ApiResultHandler.buildApiResult(200, "选课成功", studentCourse);
        } else {
            return ApiResultHandler.buildApiResult(400, "选课失败", null);
        }
    }

    /**
     * 学生退课
     */
    @DeleteMapping("/student/course/{courseId}")
    public ApiResult delete(@PathVariable Integer courseId, HttpServletRequest request) {
        // 获取当前登录学生ID
        Integer studentId = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("rb_student_id".equals(cookie.getName())) {
                    try {
                        studentId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        // 忽略
                    }
                }
            }
        }
        
        if (studentId == null) {
            return ApiResultHandler.buildApiResult(400, "未登录或不是学生", null);
        }
        
        int res = studentCourseService.delete(studentId, courseId);
        if (res > 0) {
            return ApiResultHandler.buildApiResult(200, "退课成功", null);
        } else {
            return ApiResultHandler.buildApiResult(400, "退课失败", null);
        }
    }

    /**
     * 查询学生已选的课程列表
     */
    @GetMapping("/student/courses")
    public ApiResult findCoursesByStudent(HttpServletRequest request) {
        // 获取当前登录学生ID
        Integer studentId = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("rb_student_id".equals(cookie.getName())) {
                    try {
                        studentId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        // 忽略
                    }
                }
            }
        }
        
        if (studentId == null) {
            return ApiResultHandler.buildApiResult(400, "未登录或不是学生", null);
        }
        
        List<Course> courses = studentCourseService.findCoursesByStudentId(studentId);
        return ApiResultHandler.buildApiResult(200, "请求成功", courses);
    }
}


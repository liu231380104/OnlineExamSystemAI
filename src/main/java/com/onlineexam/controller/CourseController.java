package com.onlineexam.controller;

import com.onlineexam.entity.ApiResult;
import com.onlineexam.entity.Course;
import com.onlineexam.serviceimpl.CourseServiceImpl;
import com.onlineexam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController {
    @Autowired
    private CourseServiceImpl courseService;

    @GetMapping("/courses")
    public ApiResult findAll() {
        List<Course> courses = courseService.findAll();
        return ApiResultHandler.buildApiResult(200, "请求成功", courses);
    }

    /**
     * 获取所有课程及其对应的教师列表（用于学生选课）
     */
    @GetMapping("/courses/with-teachers")
    public ApiResult findAllWithTeachers() {
        List<Course> courses = courseService.findAll();
        // 这里需要返回课程和教师的关联信息
        // 为了简化，我们返回课程列表，前端可以通过 /course/{courseId}/teachers 获取教师列表
        return ApiResultHandler.buildApiResult(200, "请求成功", courses);
    }

    @GetMapping("/course/{courseId}")
    public ApiResult findById(@PathVariable Integer courseId) {
        Course course = courseService.findById(courseId);
        if (course == null) {
            return ApiResultHandler.buildApiResult(400, "课程不存在", null);
        }
        return ApiResultHandler.buildApiResult(200, "请求成功", course);
    }

    @PostMapping("/course")
    public ApiResult add(@RequestBody Course course) {
        // 检查课程代码是否已存在
        Course existing = courseService.findByCode(course.getCourseCode());
        if (existing != null) {
            return ApiResultHandler.buildApiResult(400, "课程代码已存在", null);
        }
        int res = courseService.add(course);
        if (res > 0) {
            return ApiResultHandler.buildApiResult(200, "添加成功", course);
        } else {
            return ApiResultHandler.buildApiResult(400, "添加失败", null);
        }
    }

    @PutMapping("/course")
    public ApiResult update(@RequestBody Course course) {
        int res = courseService.update(course);
        if (res > 0) {
            return ApiResultHandler.buildApiResult(200, "更新成功", course);
        } else {
            return ApiResultHandler.buildApiResult(400, "更新失败", null);
        }
    }

    @DeleteMapping("/course/{courseId}")
    public ApiResult delete(@PathVariable Integer courseId) {
        int res = courseService.delete(courseId);
        if (res > 0) {
            return ApiResultHandler.buildApiResult(200, "删除成功", null);
        } else {
            return ApiResultHandler.buildApiResult(400, "删除失败", null);
        }
    }
}


package com.onlineexam.controller;

import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.ApiResult;
import com.onlineexam.entity.Student;
import com.onlineexam.entity.Teacher;
import com.onlineexam.serviceimpl.StudentServiceImpl;
import com.onlineexam.serviceimpl.TeacherServiceImpl;
import com.onlineexam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TeacherController {

    private TeacherServiceImpl teacherService;
    private StudentServiceImpl studentService;
    
    @Autowired
    public TeacherController(TeacherServiceImpl teacherService, StudentServiceImpl studentService){
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @GetMapping("/teachers/{page}/{size}")
    public ApiResult findAll(@PathVariable Integer page, @PathVariable Integer size){
        PageInfo<Teacher> teacherPageInfo = teacherService.findAll(page, size);

        return ApiResultHandler.buildApiResult(200,"查询所有教师",teacherPageInfo);
    }

    @GetMapping("/teacher/{teacherId}")
    public ApiResult findById(@PathVariable("teacherId") Integer teacherId){
        return ApiResultHandler.success(teacherService.findById(teacherId));
    }

    @DeleteMapping("/teacher/{teacherId}")
    public ApiResult deleteById(@PathVariable("teacherId") Integer teacherId){
        return ApiResultHandler.success(teacherService.deleteById(teacherId));
    }

    @PutMapping("/teacher")
    public ApiResult update(@RequestBody Teacher teacher){
        return ApiResultHandler.success(teacherService.update(teacher));
    }

    @PostMapping("/teacher")
    public ApiResult add(@RequestBody Teacher teacher){
        return ApiResultHandler.success(teacherService.add(teacher));
    }
    
    @GetMapping("/teacher/students")
    public ApiResult getTeacherStudents(
            @RequestParam Integer teacherId,
            @RequestParam Integer current,
            @RequestParam Integer size,
            @RequestParam String name,
            @RequestParam String grade,
            @RequestParam String tel,
            @RequestParam String institute,
            @RequestParam String major,
            @RequestParam String clazz) {
        // 改为按教师授课关系查询学生（不按学院限制）
        PageInfo<Student> studentPageInfo = studentService.findByTeacher(
                current, size, teacherId, name, grade, tel, institute, major, clazz);
        return ApiResultHandler.buildApiResult(200, "查询教师授课学生列表", studentPageInfo);
    }
}

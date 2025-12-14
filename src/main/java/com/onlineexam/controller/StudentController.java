package com.onlineexam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlineexam.entity.ApiResult;
import com.onlineexam.entity.Student;
import com.onlineexam.entity.Teacher;
import com.onlineexam.serviceimpl.StudentServiceImpl;
import com.onlineexam.serviceimpl.TeacherServiceImpl;
import com.onlineexam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public class StudentController {

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private TeacherServiceImpl teacherService;

    @GetMapping("/students/{page}/{size}/{name}/{grade}/{tel}/{institute}/{major}/{clazz}")
    public ApiResult findAll(@PathVariable Integer page, @PathVariable Integer size,
                             @PathVariable  String name, @PathVariable String grade,
                             @PathVariable String tel, @PathVariable String institute,
                             @PathVariable String major, @PathVariable String clazz,
                             HttpServletRequest request) {
        Page<Student> studentPage = new Page<>(page,size);
        
        // 获取当前登录用户的角色和教师信息
        String role = null;
        String teacherInstitute = null;
        if (request.getCookies() != null) {
            Integer teacherId = null;
            for (Cookie cookie : request.getCookies()) {
                if ("rb_role".equals(cookie.getName())) {
                    role = cookie.getValue();
                } else if ("rb_teacher_id".equals(cookie.getName())) {
                    try {
                        teacherId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        // 忽略
                    }
                }
            }
            // 只有教师（role="1"）才需要按学院过滤，管理员（role="0"）看所有学生
            if ("1".equals(role) && teacherId != null) {
                Teacher teacher = teacherService.findById(teacherId);
                if (teacher != null) {
                    teacherInstitute = teacher.getInstitute();
                }
            }
        }
        
        // 管理员不传teacherInstitute（null），会查询所有学生；教师传teacherInstitute，只查询同学院学生
        IPage<Student> res = studentService.findAll(
                studentPage, name, grade, tel, institute, major, clazz, teacherInstitute
        );
        return  ApiResultHandler.buildApiResult(200,"分页查询所有学生",res);
    }

    @GetMapping("/student/{studentId}")
    public ApiResult findById(@PathVariable("studentId") Integer studentId) {
        Student res = studentService.findById(studentId);
        if (res != null) {
        return ApiResultHandler.buildApiResult(200,"请求成功",res);
        } else {
            return ApiResultHandler.buildApiResult(404,"查询的用户不存在",null);
        }
    }

    @DeleteMapping("/student/{studentId}")
    public ApiResult deleteById(@PathVariable("studentId") Integer studentId) {
        return ApiResultHandler.buildApiResult(200,"删除成功",studentService.deleteById(studentId));
    }

    @PutMapping("/studentPWD")
    public ApiResult updatePwd(@RequestBody Student student) {
        studentService.updatePwd(student);
        return ApiResultHandler.buildApiResult(200,"密码更新成功",null);
    }
    @PutMapping("/student")
    public ApiResult update(@RequestBody Student student) {
        int res = studentService.update(student);
        if (res != 0) {
            return ApiResultHandler.buildApiResult(200,"更新成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"更新失败",res);
    }

    @PostMapping("/student")
    public ApiResult add(@RequestBody Student student) {
        int res = studentService.add(student);
        if (res == 1) {
            return ApiResultHandler.buildApiResult(200,"添加成功",null);
        }else {
            return ApiResultHandler.buildApiResult(400,"添加失败",null);
        }
    }
}

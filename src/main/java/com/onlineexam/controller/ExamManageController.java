package com.onlineexam.controller;

import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.ApiResult;
import com.onlineexam.entity.ExamManage;
import com.onlineexam.entity.Teacher;
import com.onlineexam.serviceimpl.ExamManageServiceImpl;
import com.onlineexam.serviceimpl.StudentCourseServiceImpl;
import com.onlineexam.serviceimpl.TeacherCourseServiceImpl;
import com.onlineexam.serviceimpl.TeacherServiceImpl;
import com.onlineexam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ExamManageController {

    @Autowired
    private ExamManageServiceImpl examManageService;

    @Autowired
    private TeacherServiceImpl teacherService;

    @Autowired
    private StudentCourseServiceImpl studentCourseService;

    @Autowired
    private TeacherCourseServiceImpl teacherCourseService;

    @GetMapping("/exams")
    public ApiResult findAll(HttpServletRequest request){
        System.out.println("不分页查询所有试卷");
        ApiResult apiResult;
        
        // 获取当前登录用户角色和ID
        String role = null;
        Integer teacherId = null;
        Integer studentId = null;
        String teacherInstitute = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("rb_role".equals(cookie.getName())) {
                    role = cookie.getValue();
                } else if ("rb_teacher_id".equals(cookie.getName())) {
                    try {
                        teacherId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        // 忽略
                    }
                } else if ("rb_student_id".equals(cookie.getName())) {
                    try {
                        studentId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        // 忽略
                    }
                }
            }
            // 教师不再按学院过滤，使用授课关系确定可见考试
            if ("1".equals(role) && teacherId != null) {
                teacherInstitute = null;
            }
        }
        
        // 根据角色进行过滤
        if ("2".equals(role) && studentId != null) {
            // 学生：只返回已选课程的试卷
            PageInfo<ExamManage> all = examManageService.findByStudentId(1, 9999, studentId);
            apiResult = ApiResultHandler.buildApiResult(200, "请求成功！", all.getList());
        } else if ("1".equals(role) && teacherId != null) {
            // 教师：只返回自己教授的课程的试卷（不按学院限制）
            PageInfo<ExamManage> all = examManageService.findByTeacherCourses(1, 9999, teacherId, null);
            apiResult = ApiResultHandler.buildApiResult(200, "请求成功！", all.getList());
        } else {
            // 管理员看所有试卷
            apiResult = ApiResultHandler.buildApiResult(200, "请求成功！", examManageService.findAll());
        }
        return apiResult;
    }

    @GetMapping("/exams/{page}/{size}")
    public ApiResult findAll(@PathVariable("page") Integer page, @PathVariable("size") Integer size,
                            HttpServletRequest request){
        System.out.println("分页查询所有试卷");
        ApiResult apiResult;
        
        // 获取当前登录用户角色和ID
        String role = null;
        Integer teacherId = null;
        Integer studentId = null;
        String teacherInstitute = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("rb_role".equals(cookie.getName())) {
                    role = cookie.getValue();
                } else if ("rb_teacher_id".equals(cookie.getName())) {
                    try {
                        teacherId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        // 忽略
                    }
                } else if ("rb_student_id".equals(cookie.getName())) {
                    try {
                        studentId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        // 忽略
                    }
                }
            }
            // 教师不再按学院过滤，使用授课关系确定可见考试
            if ("1".equals(role) && teacherId != null) {
                teacherInstitute = null;
            }
        }
        
        // 根据角色进行过滤
        PageInfo<ExamManage> all;
        if ("2".equals(role) && studentId != null) {
            // 学生：只返回已选课程的试卷
            all = examManageService.findByStudentId(page, size, studentId);
        } else if ("1".equals(role) && teacherId != null) {
            // 教师：只返回自己教授的课程的试卷（不按学院限制）
            all = examManageService.findByTeacherCourses(page, size, teacherId, null);
        } else {
            // 管理员看所有试卷
            all = examManageService.findAll(page, size);
        }
        apiResult = ApiResultHandler.buildApiResult(200, "请求成功！", all);
        return apiResult;
    }

    @GetMapping("/exam/{examCode}")
    public ApiResult findById(@PathVariable("examCode") Integer examCode){
        System.out.println("根据ID查找");
        ExamManage res = examManageService.findById(examCode);
        if(res == null) {
            return ApiResultHandler.buildApiResult(10000,"考试编号不存在",null);
        }
        return ApiResultHandler.buildApiResult(200,"请求成功！",res);
    }

    @DeleteMapping("/exam/{examCode}")
    public ApiResult deleteById(@PathVariable("examCode") Integer examCode){
        int res = examManageService.delete(examCode);
        return ApiResultHandler.buildApiResult(200,"删除成功",res);
    }

    @PutMapping("/exam")
    public ApiResult update(@RequestBody ExamManage exammanage){
        int res = examManageService.update(exammanage);
//        if (res == 0) {
//            return ApiResultHandler.buildApiResult(20000,"请求参数错误");
//        }
        System.out.print("更新操作执行---");
        return ApiResultHandler.buildApiResult(200,"更新成功",res);
    }

    @PostMapping("/exam")
    public ApiResult add(@RequestBody ExamManage exammanage, HttpServletRequest request){
        // 如果是教师添加考试，自动设置teacherId并验证课程
        if (request.getCookies() != null) {
            String role = null;
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
            // 如果是教师，自动设置teacherId并验证课程
            if ("1".equals(role) && teacherId != null) {
                exammanage.setTeacherId(teacherId);
                // 教师发布试卷时，courseId是必填的
                if (exammanage.getCourseId() == null) {
                    return ApiResultHandler.buildApiResult(400, "请选择所属课程", null);
                }
                // 验证该教师是否教授该课程
                int count = teacherCourseService.checkTeacherCourse(teacherId, exammanage.getCourseId());
                if (count == 0) {
                    return ApiResultHandler.buildApiResult(400, "您没有教授该课程的权限，请选择您教授的课程", null);
                }
            }
        }
        
        int res = examManageService.add(exammanage);
        if (res ==1) {
            return ApiResultHandler.buildApiResult(200, "添加成功", res);
        } else {
            return  ApiResultHandler.buildApiResult(400,"添加失败",res);
        }
    }

    @GetMapping("/examManagePaperId")
    public ApiResult findOnlyPaperId() {
        ExamManage res = examManageService.findOnlyPaperId();
        if (res != null) {
            return ApiResultHandler.buildApiResult(200,"请求成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"请求失败",res);
    }
}

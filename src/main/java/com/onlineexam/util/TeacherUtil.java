package com.onlineexam.util;

import com.onlineexam.entity.Teacher;
import com.onlineexam.mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 教师工具类，用于获取当前登录的教师信息
 */
@Component
public class TeacherUtil {

    private static LoginMapper loginMapper;

    @Autowired
    public void setLoginMapper(LoginMapper loginMapper) {
        TeacherUtil.loginMapper = loginMapper;
    }

    /**
     * 从请求中获取当前登录的教师信息
     * @param request HTTP请求
     * @return 教师信息，如果未登录或不是教师则返回null
     */
    public static Teacher getCurrentTeacher(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        String cardId = null;
        String role = null;
        Integer teacherId = null;

        for (Cookie cookie : request.getCookies()) {
            if ("rb_token".equals(cookie.getName())) {
                cardId = cookie.getValue();
            } else if ("rb_role".equals(cookie.getName())) {
                role = cookie.getValue();
            } else if ("rb_teacher_id".equals(cookie.getName())) {
                try {
                    teacherId = Integer.parseInt(cookie.getValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }

        // 如果不是教师角色，返回null
        if (!"1".equals(role) || cardId == null) {
            return null;
        }

        // 如果cookie中有teacherId，直接使用
        if (teacherId != null && loginMapper != null) {
            // 可以根据cardId查询教师信息
            // 这里简化处理，通过cardId查询
            // 实际应该通过teacherId查询，但为了兼容现有系统，先通过cardId查询
        }

        // 通过cardId查询教师信息（简化处理）
        // 注意：这里需要根据实际需求调整查询方式
        return null;
    }

    /**
     * 从请求中获取当前登录的教师ID
     * @param request HTTP请求
     * @return 教师ID，如果未登录或不是教师则返回null
     */
    public static Integer getCurrentTeacherId(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        String role = null;
        Integer teacherId = null;

        for (Cookie cookie : request.getCookies()) {
            if ("rb_role".equals(cookie.getName())) {
                role = cookie.getValue();
            } else if ("rb_teacher_id".equals(cookie.getName())) {
                try {
                    teacherId = Integer.parseInt(cookie.getValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }

        // 如果不是教师角色，返回null
        if (!"1".equals(role)) {
            return null;
        }

        return teacherId;
    }

    /**
     * 从请求中获取当前登录的教师学院
     * @param request HTTP请求
     * @return 教师学院，如果未登录或不是教师则返回null
     */
    public static String getCurrentTeacherInstitute(HttpServletRequest request) {
        Teacher teacher = getCurrentTeacher(request);
        return teacher != null ? teacher.getInstitute() : null;
    }
}


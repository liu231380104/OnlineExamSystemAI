package com.onlineexam.service;

import com.onlineexam.entity.Admin;
import com.onlineexam.entity.Student;
import com.onlineexam.entity.Teacher;

public interface LoginService {

    public Admin adminLogin(Integer username, String password);

    public Teacher teacherLogin(Integer username, String password);

    public Student studentLogin(Integer username, String password);
}

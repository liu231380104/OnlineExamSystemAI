package com.onlineexam.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.Student;
import com.onlineexam.mapper.StudentMapper;
import com.onlineexam.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;


    @Override
    public PageInfo<Student> findAll(
            Integer page, Integer size, String name, String grade,
            String tel, String institute, String major, String clazz, String teacherInstitute) {
        name = ("@".equals(name) ? "" : name);
        grade = ("@".equals(grade) ? "" : grade);
        tel = ("@".equals(tel) ? "" : tel);
        institute = ("@".equals(institute) ? "" : institute);
        major = ("@".equals(major) ? "" : major);
        clazz = ("@".equals(clazz) ? "" : clazz);
        
        PageHelper.startPage(page, size);
        List<Student> students = studentMapper.findAll(name, grade, tel, institute, major, clazz, teacherInstitute);
        return new PageInfo<>(students);
    }

    @Override
    public PageInfo<Student> findByTeacher(Integer page, Integer size, Integer teacherId,
                                           String name, String grade, String tel,
                                           String institute, String major, String clazz) {
        name = ("@".equals(name) ? "" : name);
        grade = ("@".equals(grade) ? "" : grade);
        tel = ("@".equals(tel) ? "" : tel);
        institute = ("@".equals(institute) ? "" : institute);
        major = ("@".equals(major) ? "" : major);
        clazz = ("@".equals(clazz) ? "" : clazz);

        PageHelper.startPage(page, size);
        List<Student> students = studentMapper.findByTeacherWithFilters(teacherId, name, grade, tel, institute, major, clazz);
        return new PageInfo<>(students);
    }

    @Override
    public Student findById(Integer studentId) {
        return studentMapper.findById(studentId);
    }

    @Override
    public int deleteById(Integer studentId) {
        return studentMapper.deleteById(studentId);
    }

    @Override
    public int update(Student student) {
        return studentMapper.update(student);
    }

    @Override
    public int updatePwd(Student student) {
        return studentMapper.updatePwd(student);
    }

    @Override
    public int add(Student student) {
        return studentMapper.add(student);
    }
}

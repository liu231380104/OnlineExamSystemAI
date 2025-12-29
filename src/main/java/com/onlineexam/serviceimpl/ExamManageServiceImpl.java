package com.onlineexam.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlineexam.entity.ExamManage;
import com.onlineexam.mapper.ExamManageMapper;
import com.onlineexam.service.ExamManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamManageServiceImpl implements ExamManageService {
    @Autowired
    private ExamManageMapper examManageMapper;
    @Autowired
    private PaperServiceImpl paperService;

    private void setMaxScore(List<ExamManage> examManageList) {
        for (ExamManage examManage : examManageList) {
            examManage.setTotalScore(paperService.getMaxScore(examManage.getPaperId()));
        }
    }

    @Override
    public List<ExamManage> findAll() {
        List<ExamManage> examManageList = examManageMapper.findAll();
        setMaxScore(examManageList);
        return examManageList;
    }

    @Override
    public PageInfo<ExamManage> findAll(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<ExamManage> examManageList = examManageMapper.findAll();
        setMaxScore(examManageList);
        return new PageInfo<>(examManageList);
    }

    @Override
    public ExamManage findById(Integer examCode) {
        ExamManage examManage = examManageMapper.findById(examCode);
        examManage.setTotalScore(paperService.getMaxScore(examManage.getPaperId()));
        return examManage;
    }

    @Override
    public int delete(Integer examCode) {
        // 移除题目关联
        ExamManage examManage = examManageMapper.findById(examCode);
        if(examManage == null) {
            return 0;
        }
        paperService.deleteByPaperId(examManage.getPaperId());
        return examManageMapper.delete(examCode);
    }

    @Override
    public int update(ExamManage exammanage) {
        return examManageMapper.update(exammanage);
    }

    @Override
    public int add(ExamManage exammanage) {
        return examManageMapper.add(exammanage);
    }

    @Override
    public ExamManage findOnlyPaperId() {
//        int max=999999;
//        int min=1;
//        Random random = new Random();
//        ExamManage examManage = new ExamManage();
//        examManage.setPaperId(random.nextInt(max)%(max-min+1) + min);
//        return examManage;
        return examManageMapper.findOnlyPaperId();
    }

    @Override
    public PageInfo<ExamManage> findByTeacher(Integer page, Integer size, Integer teacherId, String teacherInstitute) {
        PageHelper.startPage(page, size);
        List<ExamManage> examManageList = examManageMapper.findByTeacher(teacherId, teacherInstitute);
        setMaxScore(examManageList);
        return new PageInfo<>(examManageList);
    }

    @Override
    public PageInfo<ExamManage> findByStudentId(Integer page, Integer size, Integer studentId) {
        PageHelper.startPage(page, size);
        List<ExamManage> examManageList = examManageMapper.findByStudentId(studentId);
        setMaxScore(examManageList);
        return new PageInfo<>(examManageList);
    }

    @Override
    public PageInfo<ExamManage> findByTeacherCourses(Integer page, Integer size, Integer teacherId, String teacherInstitute) {
        PageHelper.startPage(page, size);
        List<ExamManage> examManageList = examManageMapper.findByTeacherCourses(teacherId, teacherInstitute);
        setMaxScore(examManageList);
        return new PageInfo<>(examManageList);
    }
}

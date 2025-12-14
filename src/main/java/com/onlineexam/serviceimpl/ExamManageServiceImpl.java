package com.onlineexam.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        Page<ExamManage> examManage = new Page<>(0,9999);
        List<ExamManage> examManageList = examManageMapper.findAll(examManage).getRecords();
        setMaxScore(examManageList);
        return examManageList;
    }

    @Override
    public IPage<ExamManage> findAll(Page<ExamManage> page) {
        IPage<ExamManage> iPage = examManageMapper.findAll(page);
        setMaxScore(iPage.getRecords());
        return iPage;
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
    public IPage<ExamManage> findByTeacher(Page<ExamManage> page, Integer teacherId, String teacherInstitute) {
        IPage<ExamManage> iPage = examManageMapper.findByTeacher(page, teacherId, teacherInstitute);
        setMaxScore(iPage.getRecords());
        return iPage;
    }

    @Override
    public IPage<ExamManage> findByStudentId(Page<ExamManage> page, Integer studentId) {
        IPage<ExamManage> iPage = examManageMapper.findByStudentId(page, studentId);
        setMaxScore(iPage.getRecords());
        return iPage;
    }

    @Override
    public IPage<ExamManage> findByTeacherCourses(Page<ExamManage> page, Integer teacherId, String teacherInstitute) {
        IPage<ExamManage> iPage = examManageMapper.findByTeacherCourses(page, teacherId, teacherInstitute);
        setMaxScore(iPage.getRecords());
        return iPage;
    }
}

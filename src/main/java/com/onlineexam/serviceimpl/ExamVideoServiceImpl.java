package com.onlineexam.serviceimpl;

import com.onlineexam.entity.ExamVideo;
import com.onlineexam.mapper.ExamVideoMapper;
import com.onlineexam.service.ExamVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamVideoServiceImpl implements ExamVideoService {

    @Autowired
    private ExamVideoMapper examVideoMapper;

    @Override
    public int add(ExamVideo examVideo) {
        return examVideoMapper.add(examVideo);
    }

    @Override
    public List<ExamVideo> findByExamCodeAndStudentId(Integer examCode, Integer studentId) {
        return examVideoMapper.findByExamCodeAndStudentId(examCode, studentId);
    }

    @Override
    public List<ExamVideo> findByExamCode(Integer examCode) {
        return examVideoMapper.findByExamCode(examCode);
    }

    @Override
    public List<ExamVideo> findByStudentId(Integer studentId) {
        return examVideoMapper.findByStudentId(studentId);
    }

    @Override
    public ExamVideo findByVideoId(Integer videoId) {
        return examVideoMapper.findByVideoId(videoId);
    }
}


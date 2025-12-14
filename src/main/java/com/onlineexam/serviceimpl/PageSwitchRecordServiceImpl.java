package com.onlineexam.serviceimpl;

import com.onlineexam.entity.PageSwitchRecord;
import com.onlineexam.mapper.PageSwitchRecordMapper;
import com.onlineexam.service.PageSwitchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageSwitchRecordServiceImpl implements PageSwitchRecordService {

    @Autowired
    private PageSwitchRecordMapper pageSwitchRecordMapper;

    @Override
    public int add(PageSwitchRecord record) {
        return pageSwitchRecordMapper.add(record);
    }

    @Override
    public List<PageSwitchRecord> findByExamCode(Integer examCode) {
        return pageSwitchRecordMapper.findByExamCode(examCode);
    }

    @Override
    public List<PageSwitchRecord> findByExamCodeAndStudentId(Integer examCode, Integer studentId) {
        return pageSwitchRecordMapper.findByExamCodeAndStudentId(examCode, studentId);
    }

    @Override
    public List<PageSwitchRecord> findByStudentId(Integer studentId) {
        return pageSwitchRecordMapper.findByStudentId(studentId);
    }

    @Override
    public int updateReturnTime(Integer examCode, Integer studentId, String returnTime, Integer duration) {
        return pageSwitchRecordMapper.updateReturnTime(examCode, studentId, returnTime, duration);
    }
}


package com.onlineexam.service;

import com.onlineexam.entity.PageSwitchRecord;

import java.util.List;

public interface PageSwitchRecordService {
    /**
     * 添加页面切换记录
     * @param record 切换记录
     * @return
     */
    int add(PageSwitchRecord record);

    /**
     * 根据考试编号查询所有学生的页面切换记录
     * @param examCode 考试编号
     * @return
     */
    List<PageSwitchRecord> findByExamCode(Integer examCode);

    /**
     * 根据考试编号和学生ID查询页面切换记录
     * @param examCode 考试编号
     * @param studentId 学生ID
     * @return
     */
    List<PageSwitchRecord> findByExamCodeAndStudentId(Integer examCode, Integer studentId);

    /**
     * 根据学生ID查询所有页面切换记录
     * @param studentId 学生ID
     * @return
     */
    List<PageSwitchRecord> findByStudentId(Integer studentId);

    /**
     * 更新页面切换记录的返回时间和离开时长
     * @param examCode 考试编号
     * @param studentId 学生ID
     * @param returnTime 返回时间
     * @param duration 离开时长（秒）
     * @return
     */
    int updateReturnTime(Integer examCode, Integer studentId, String returnTime, Integer duration);
}


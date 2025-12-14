package com.onlineexam.controller;

import com.onlineexam.entity.ApiResult;
import com.onlineexam.entity.PageSwitchRecord;
import com.onlineexam.serviceimpl.PageSwitchRecordServiceImpl;
import com.onlineexam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PageSwitchRecordController {

    @Autowired
    private PageSwitchRecordServiceImpl pageSwitchRecordService;

    /**
     * 记录页面切换
     * @param record 切换记录
     * @return
     */
    @PostMapping("/page/switch/record")
    public ApiResult recordPageSwitch(@RequestBody PageSwitchRecord record) {
        int result = pageSwitchRecordService.add(record);
        if (result > 0) {
            return ApiResultHandler.buildApiResult(200, "记录成功", record);
        } else {
            return ApiResultHandler.buildApiResult(400, "记录失败", null);
        }
    }

    /**
     * 根据考试编号查询所有学生的页面切换记录
     * @param examCode 考试编号
     * @return
     */
    @GetMapping("/page/switch/record/{examCode}")
    public ApiResult getRecordsByExam(@PathVariable("examCode") Integer examCode) {
        List<PageSwitchRecord> records = pageSwitchRecordService.findByExamCode(examCode);
        return ApiResultHandler.buildApiResult(200, "查询成功", records);
    }

    /**
     * 根据考试编号和学生ID查询页面切换记录
     * @param examCode 考试编号
     * @param studentId 学生ID
     * @return
     */
    @GetMapping("/page/switch/record/{examCode}/{studentId}")
    public ApiResult getRecordsByExamAndStudent(
            @PathVariable("examCode") Integer examCode,
            @PathVariable("studentId") Integer studentId) {
        List<PageSwitchRecord> records = pageSwitchRecordService.findByExamCodeAndStudentId(examCode, studentId);
        return ApiResultHandler.buildApiResult(200, "查询成功", records);
    }

    /**
     * 根据学生ID查询所有页面切换记录
     * @param studentId 学生ID
     * @return
     */
    @GetMapping("/page/switch/record/student/{studentId}")
    public ApiResult getRecordsByStudent(@PathVariable("studentId") Integer studentId) {
        List<PageSwitchRecord> records = pageSwitchRecordService.findByStudentId(studentId);
        return ApiResultHandler.buildApiResult(200, "查询成功", records);
    }

    /**
     * 更新页面切换记录的返回时间和离开时长
     * @param examCode 考试编号
     * @param studentId 学生ID
     * @param returnTime 返回时间
     * @param duration 离开时长（秒）
     * @return
     */
    @PutMapping("/page/switch/record/return")
    public ApiResult updateReturnTime(@RequestParam("examCode") Integer examCode,
                                      @RequestParam("studentId") Integer studentId,
                                      @RequestParam("returnTime") String returnTime,
                                      @RequestParam("duration") Integer duration) {
        int result = pageSwitchRecordService.updateReturnTime(examCode, studentId, returnTime, duration);
        if (result > 0) {
            return ApiResultHandler.buildApiResult(200, "更新成功", result);
        } else {
            return ApiResultHandler.buildApiResult(400, "更新失败，可能没有找到对应的记录", null);
        }
    }
}


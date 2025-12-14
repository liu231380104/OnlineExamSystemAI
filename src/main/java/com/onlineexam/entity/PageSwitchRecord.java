package com.onlineexam.entity;

public class PageSwitchRecord {
    private Integer recordId;
    private Integer examCode;
    private Integer studentId;
    private String switchTime;
    private String switchType; // tab_switch(切换标签页), window_blur(窗口失焦), page_leave(离开页面)
    private Integer duration; // 离开时长（秒）
    private String returnTime;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getExamCode() {
        return examCode;
    }

    public void setExamCode(Integer examCode) {
        this.examCode = examCode;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getSwitchTime() {
        return switchTime;
    }

    public void setSwitchTime(String switchTime) {
        this.switchTime = switchTime;
    }

    public String getSwitchType() {
        return switchType;
    }

    public void setSwitchType(String switchType) {
        this.switchType = switchType;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    @Override
    public String toString() {
        return "PageSwitchRecord{" +
                "recordId=" + recordId +
                ", examCode=" + examCode +
                ", studentId=" + studentId +
                ", switchTime='" + switchTime + '\'' +
                ", switchType='" + switchType + '\'' +
                ", duration=" + duration +
                ", returnTime='" + returnTime + '\'' +
                '}';
    }
}


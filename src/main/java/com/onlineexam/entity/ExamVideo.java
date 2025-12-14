package com.onlineexam.entity;

public class ExamVideo {
    private Integer videoId;
    private Integer examCode;
    private Integer studentId;
    private String videoPath;
    private String videoName;
    private String captureTime;
    private Integer duration;
    private String uploadTime;

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
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

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(String captureTime) {
        this.captureTime = captureTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Override
    public String toString() {
        return "ExamVideo{" +
                "videoId=" + videoId +
                ", examCode=" + examCode +
                ", studentId=" + studentId +
                ", videoPath='" + videoPath + '\'' +
                ", videoName='" + videoName + '\'' +
                ", captureTime='" + captureTime + '\'' +
                ", duration=" + duration +
                ", uploadTime='" + uploadTime + '\'' +
                '}';
    }
}


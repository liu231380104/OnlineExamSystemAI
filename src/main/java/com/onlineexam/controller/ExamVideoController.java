package com.onlineexam.controller;

import com.onlineexam.entity.ApiResult;
import com.onlineexam.entity.ExamVideo;
import com.onlineexam.serviceimpl.ExamVideoServiceImpl;
import com.onlineexam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class ExamVideoController {

    @Autowired
    private ExamVideoServiceImpl examVideoService;

    @Value("${video.upload.path:./videos}")
    private String uploadPath;

    /**
     * 上传考试监控视频
     * @param file 视频文件
     * @param examCode 考试编号
     * @param studentId 学生ID
     * @param duration 视频时长（秒）
     * @return
     */
    @PostMapping("/exam/video/upload")
    public ApiResult uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("examCode") Integer examCode,
            @RequestParam("studentId") Integer studentId,
            @RequestParam(value = "duration", defaultValue = "5") Integer duration) {
        
        if (file.isEmpty()) {
            return ApiResultHandler.buildApiResult(400, "视频文件为空", null);
        }

        try {
            // 处理上传路径，确保使用绝对路径
            File baseUploadDir;
            if (uploadPath.startsWith(".")) {
                // 如果是相对路径，转换为项目根目录的绝对路径
                String projectRoot = System.getProperty("user.dir");
                String relativePath = uploadPath.substring(1); // 去掉开头的 "."
                baseUploadDir = new File(projectRoot + relativePath);
            } else {
                baseUploadDir = new File(uploadPath);
            }
            
            // 创建基础上传目录
            if (!baseUploadDir.exists()) {
                boolean created = baseUploadDir.mkdirs();
                System.out.println("创建基础上传目录: " + baseUploadDir.getAbsolutePath() + ", 结果: " + created);
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            } else {
                extension = ".webm"; // 默认扩展名
            }
            String fileName = UUID.randomUUID().toString() + extension;
            
            // 按考试编号和学生ID组织目录结构
            File examDirFile = new File(baseUploadDir, String.valueOf(examCode));
            if (!examDirFile.exists()) {
                boolean created = examDirFile.mkdirs();
                System.out.println("创建考试目录: " + examDirFile.getAbsolutePath() + ", 结果: " + created);
            }
            
            File studentDirFile = new File(examDirFile, String.valueOf(studentId));
            if (!studentDirFile.exists()) {
                boolean created = studentDirFile.mkdirs();
                System.out.println("创建学生目录: " + studentDirFile.getAbsolutePath() + ", 结果: " + created);
            }

            // 保存文件
            File destFile = new File(studentDirFile, fileName);
            System.out.println("准备保存文件到: " + destFile.getAbsolutePath());
            System.out.println("文件是否存在: " + destFile.exists());
            System.out.println("父目录是否存在: " + destFile.getParentFile().exists());
            
            file.transferTo(destFile);
            System.out.println("文件保存成功: " + destFile.getAbsolutePath() + ", 大小: " + destFile.length() + " 字节");

            // 保存视频记录到数据库
            ExamVideo examVideo = new ExamVideo();
            examVideo.setExamCode(examCode);
            examVideo.setStudentId(studentId);
            // 使用相对路径，便于前端访问
            examVideo.setVideoPath("/videos/" + examCode + "/" + studentId + "/" + fileName);
            examVideo.setVideoName(originalFilename != null ? originalFilename : fileName);
            examVideo.setDuration(duration);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = sdf.format(new Date());
            examVideo.setCaptureTime(now);
            examVideo.setUploadTime(now);

            int result = examVideoService.add(examVideo);
            
            if (result > 0) {
                return ApiResultHandler.buildApiResult(200, "视频上传成功", examVideo);
            } else {
                // 如果数据库保存失败，删除已上传的文件
                destFile.delete();
                return ApiResultHandler.buildApiResult(400, "视频上传失败", null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResultHandler.buildApiResult(500, "视频上传异常: " + e.getMessage(), null);
        }
    }

    /**
     * 根据考试编号和学生ID查询视频列表
     * @param examCode 考试编号
     * @param studentId 学生ID
     * @return
     */
    @GetMapping("/exam/video/{examCode}/{studentId}")
    public ApiResult getVideosByExamAndStudent(
            @PathVariable("examCode") Integer examCode,
            @PathVariable("studentId") Integer studentId) {
        List<ExamVideo> videos = examVideoService.findByExamCodeAndStudentId(examCode, studentId);
        return ApiResultHandler.buildApiResult(200, "查询成功", videos);
    }

    /**
     * 根据考试编号查询所有学生的视频
     * @param examCode 考试编号
     * @return
     */
    @GetMapping("/exam/video/{examCode}")
    public ApiResult getVideosByExam(@PathVariable("examCode") Integer examCode) {
        List<ExamVideo> videos = examVideoService.findByExamCode(examCode);
        return ApiResultHandler.buildApiResult(200, "查询成功", videos);
    }

    /**
     * 根据学生ID查询所有视频
     * @param studentId 学生ID
     * @return
     */
    @GetMapping("/exam/video/student/{studentId}")
    public ApiResult getVideosByStudent(@PathVariable("studentId") Integer studentId) {
        List<ExamVideo> videos = examVideoService.findByStudentId(studentId);
        return ApiResultHandler.buildApiResult(200, "查询成功", videos);
    }

    /**
     * 根据视频ID查询视频信息
     * @param videoId 视频ID
     * @return
     */
    @GetMapping("/exam/video/detail/{videoId}")
    public ApiResult getVideoById(@PathVariable("videoId") Integer videoId) {
        ExamVideo video = examVideoService.findByVideoId(videoId);
        if (video != null) {
            return ApiResultHandler.buildApiResult(200, "查询成功", video);
        } else {
            return ApiResultHandler.buildApiResult(404, "视频不存在", null);
        }
    }
}


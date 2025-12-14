package com.onlineexam.mapper;

import com.onlineexam.entity.Course;
import com.onlineexam.entity.StudentCourse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentCourseMapper {
    /**
     * 学生选课
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into student_course(studentId,courseId,teacherId,selectTime) " +
            "values(#{studentId},#{courseId},#{teacherId},#{selectTime})")
    int add(StudentCourse studentCourse);

    /**
     * 学生退课
     */
    @Delete("delete from student_course where studentId=#{studentId} and courseId=#{courseId}")
    int delete(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId);

    /**
     * 根据学生ID查询其选择的课程列表
     */
    @Select("select c.*, sc.teacherId, sc.selectTime " +
            "from course c " +
            "inner join student_course sc on c.courseId = sc.courseId " +
            "where sc.studentId = #{studentId} " +
            "order by sc.selectTime desc")
    List<Course> findCoursesByStudentId(@Param("studentId") Integer studentId);

    /**
     * 根据学生ID和课程ID查询选课记录
     */
    @Select("select * from student_course where studentId=#{studentId} and courseId=#{courseId}")
    StudentCourse findByStudentAndCourse(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId);

    /**
     * 根据课程ID和教师ID查询选课的学生列表
     */
    @Select("select sc.* from student_course sc " +
            "where sc.courseId=#{courseId} and sc.teacherId=#{teacherId}")
    List<StudentCourse> findStudentsByCourseAndTeacher(@Param("courseId") Integer courseId, 
                                                        @Param("teacherId") Integer teacherId);

    /**
     * 检查学生是否已选该课程
     */
    @Select("select count(*) from student_course where studentId=#{studentId} and courseId=#{courseId}")
    int checkStudentCourse(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId);

    /**
     * 根据学生ID和课程ID获取教师ID
     */
    @Select("select teacherId from student_course where studentId=#{studentId} and courseId=#{courseId}")
    Integer getTeacherIdByStudentAndCourse(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId);
}


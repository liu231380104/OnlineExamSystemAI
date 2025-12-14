package com.onlineexam.mapper;

import com.onlineexam.entity.Course;
import com.onlineexam.entity.TeacherCourse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeacherCourseMapper {
    /**
     * 添加教师课程关联
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into teacher_course(teacherId,courseId) values(#{teacherId},#{courseId})")
    int add(TeacherCourse teacherCourse);

    /**
     * 删除教师课程关联
     */
    @Delete("delete from teacher_course where teacherId=#{teacherId} and courseId=#{courseId}")
    int delete(@Param("teacherId") Integer teacherId, @Param("courseId") Integer courseId);

    /**
     * 根据教师ID查询其教授的课程列表
     */
    @Select("select c.* from course c " +
            "inner join teacher_course tc on c.courseId = tc.courseId " +
            "where tc.teacherId = #{teacherId} " +
            "order by c.courseId desc")
    List<Course> findCoursesByTeacherId(@Param("teacherId") Integer teacherId);

    /**
     * 根据课程ID查询教授该课程的教师列表
     */
    @Select("select tc.* from teacher_course tc where tc.courseId = #{courseId}")
    List<TeacherCourse> findTeachersByCourseId(@Param("courseId") Integer courseId);

    /**
     * 检查教师是否教授该课程
     */
    @Select("select count(*) from teacher_course where teacherId=#{teacherId} and courseId=#{courseId}")
    int checkTeacherCourse(@Param("teacherId") Integer teacherId, @Param("courseId") Integer courseId);
}


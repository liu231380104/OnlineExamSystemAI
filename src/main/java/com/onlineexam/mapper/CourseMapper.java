package com.onlineexam.mapper;

import com.onlineexam.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {
    /**
     * 查询所有课程
     */
    @Select("select * from course order by courseId desc")
    List<Course> findAll();

    /**
     * 根据课程ID查询
     */
    @Select("select * from course where courseId = #{courseId}")
    Course findById(@Param("courseId") Integer courseId);

    /**
     * 根据课程代码查询
     */
    @Select("select * from course where courseCode = #{courseCode}")
    Course findByCode(@Param("courseCode") String courseCode);

    /**
     * 添加课程
     */
    @Options(useGeneratedKeys = true, keyProperty = "courseId")
    @Insert("insert into course(courseName,courseCode,description,credits,institute) " +
            "values(#{courseName},#{courseCode},#{description},#{credits},#{institute})")
    int add(Course course);

    /**
     * 更新课程
     */
    @Update("update course set courseName=#{courseName},courseCode=#{courseCode}," +
            "description=#{description},credits=#{credits},institute=#{institute} " +
            "where courseId=#{courseId}")
    int update(Course course);

    /**
     * 删除课程
     */
    @Delete("delete from course where courseId = #{courseId}")
    int delete(@Param("courseId") Integer courseId);
}


package com.onlineexam.mapper;

import com.onlineexam.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeacherMapper {

    @Select("select * from teacher")
    List<Teacher>  findAll();

//    @Select("select * from teacher")
//    public List<Teacher> findAll();

    @Select("select * from teacher where teacherId = #{teacherId}")
    public Teacher findById(Integer teacherId);

    @Select("select teacherId,teacherName,institute,sex,tel,email,cardId,type,role from teacher where teacherId = #{teacherId}")
    public Teacher findByIdWithoutPwd(Integer teacherId);

    @Delete("delete from teacher where teacherId = #{teacherId}")
    public int deleteById(Integer teacherId);

    @Update("update teacher set teacherName = #{teacherName},sex = #{sex}," +
            "tel = #{tel}, email = #{email},pwd = #{pwd},cardId = #{cardId}," +
            "role = #{role},institute = #{institute},type = #{type} where teacherId = #{teacherId}")
    public int update(Teacher teacher);

    @Options(useGeneratedKeys = true,keyProperty = "teacherId")
    @Insert("insert into teacher(teacherName,sex,tel,email,pwd,cardId,role,type,institute) " +
            "values(#{teacherName},#{sex},#{tel},#{email},#{pwd},#{cardId},#{role},#{type},#{institute})")
    public int add(Teacher teacher);
}

package com.onlineexam.mapper;

import com.onlineexam.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    /**
     * 分页查询所有学生
     * @return List<Student>
     */
    @Select("select * from student where " +
            "studentName like concat('%',#{name},'%') " +
            "and grade like concat('%',#{grade},'%') " +
            "and tel like concat('%',#{tel},'%') " +
            "and major like concat('%',#{major},'%') " +
            "and institute like concat('%',#{institute},'%') " +
            "and clazz like concat('%',#{clazz},'%') " +
            "and (#{teacherInstitute} is null or #{teacherInstitute} = '' or institute = #{teacherInstitute})")
    List<Student> findAll(@Param("name") String name, @Param("grade") String grade,
                           @Param("tel") String tel,  @Param("institute") String institute,
                           @Param("major")String major, @Param("clazz") String clazz,
                           @Param("teacherInstitute") String teacherInstitute);

    /**
     * 按教师授课关系查询学生（不限制学院），并支持条件筛选
     */
    @Select("select distinct s.* from student s " +
            "inner join student_course sc on s.studentId = sc.studentId " +
            "where sc.teacherId = #{teacherId} " +
            "and s.studentName like concat('%',#{name},'%') " +
            "and s.grade like concat('%',#{grade},'%') " +
            "and s.tel like concat('%',#{tel},'%') " +
            "and s.major like concat('%',#{major},'%') " +
            "and s.institute like concat('%',#{institute},'%') " +
            "and s.clazz like concat('%',#{clazz},'%') ")
    List<Student> findByTeacherWithFilters(@Param("teacherId") Integer teacherId,
                                           @Param("name") String name, @Param("grade") String grade,
                                           @Param("tel") String tel,  @Param("institute") String institute,
                                           @Param("major")String major, @Param("clazz") String clazz);

    @Select("select * from student where studentId = #{studentId}")
    Student findById(Integer studentId);

    @Delete("delete from student where studentId = #{studentId}")
    int deleteById(Integer studentId);

    /**
     *更新所有学生信息
     * @param student 传递一个对象
     * @return 受影响的记录条数
     */
    @Update("update student set studentName = #{studentName},grade = #{grade},major = #{major},clazz = #{clazz}," +
            "institute = #{institute},tel = #{tel},email = #{email},pwd = #{pwd},cardId = #{cardId},sex = #{sex},role = #{role} " +
            "where studentId = #{studentId}")
    int update(Student student);

    /**
     * 更新密码
     * @param student
     * @return 受影响的记录条数
     */
    @Update("update student set pwd = #{pwd} where studentId = #{studentId}")
    int updatePwd(Student student);


    @Options(useGeneratedKeys = true,keyProperty = "studentId")
    @Insert("insert into student(studentName,grade,major,clazz,institute,tel,email,pwd,cardId,sex,role) values " +
            "(#{studentName},#{grade},#{major},#{clazz},#{institute},#{tel},#{email},#{pwd},#{cardId},#{sex},#{role})")
    int add(Student student);
}

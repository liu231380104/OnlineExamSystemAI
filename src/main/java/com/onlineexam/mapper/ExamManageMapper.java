package com.onlineexam.mapper;

import com.onlineexam.entity.ExamManage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExamManageMapper {
    @Select("select * from exam_manage")
    List<ExamManage> findAll();

    @Select("select * from exam_manage where " +
            "(#{teacherId} is null or teacherId = #{teacherId}) " +
            "and (#{teacherInstitute} is null or #{teacherInstitute} = '' or institute = #{teacherInstitute})")
    List<ExamManage> findByTeacher(@Param("teacherId") Integer teacherId, 
                                     @Param("teacherInstitute") String teacherInstitute);

    /**
     * 根据学生ID查询该学生已选课程的试卷
     */
    @Select("select em.* from exam_manage em " +
            "inner join student_course sc on em.courseId = sc.courseId and em.teacherId = sc.teacherId " +
            "where sc.studentId = #{studentId} " +
            "order by em.examCode desc")
    List<ExamManage> findByStudentId(@Param("studentId") Integer studentId);

    /**
     * 根据教师ID查询该教师教授的课程的试卷（通过teacher_course关联）
     */
    @Select("select distinct em.* from exam_manage em " +
            "inner join teacher_course tc on em.courseId = tc.courseId " +
            "where tc.teacherId = #{teacherId} " +
            "and (#{teacherInstitute} is null or #{teacherInstitute} = '' or em.institute = #{teacherInstitute}) " +
            "order by em.examCode desc")
    List<ExamManage> findByTeacherCourses(@Param("teacherId") Integer teacherId,
                                             @Param("teacherInstitute") String teacherInstitute);

    @Select("select * from exam_manage where examCode = #{examCode}")
    ExamManage findById(Integer examCode);

    @Delete("delete from exam_manage where examCode = #{examCode}")
    int delete(Integer examCode);

    @Update("update exam_manage set description = #{description},source = #{source},paperId = #{paperId}," +
            "examDate = #{examDate},totalTime = #{totalTime},grade = #{grade},term = #{term}," +
            "major = #{major},institute = #{institute},totalScore = #{totalScore}," +
            "type = #{type},tips = #{tips},teacherId = #{teacherId},courseId = #{courseId} where examCode = #{examCode}")
    int update(ExamManage exammanage);

    @Options(useGeneratedKeys = true,keyProperty = "examCode")
    @Insert("insert into exam_manage(description,source,paperId,examDate,totalTime,grade,term,major,institute,totalScore,type,tips,teacherId,courseId)" +
            " values(#{description},#{source},#{paperId},#{examDate},#{totalTime},#{grade},#{term},#{major},#{institute},#{totalScore},#{type},#{tips},#{teacherId},#{courseId})")
    int add(ExamManage exammanage);

    /**
     * 查询最后一条记录的paperId,返回给前端达到自增效果
     * @return paperId
     */
    @Select("select paperId from exam_manage order by paperId desc limit 1")
    ExamManage findOnlyPaperId();
}

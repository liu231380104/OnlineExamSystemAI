package com.onlineexam.mapper;

import com.onlineexam.entity.PageSwitchRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PageSwitchRecordMapper {
    /**
     * 添加一条页面切换记录
     * @param record 切换记录
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    @Insert("insert into page_switch_record(examCode,studentId,switchTime,switchType,duration,returnTime) " +
            "values(#{examCode},#{studentId},#{switchTime},#{switchType},#{duration},#{returnTime})")
    int add(PageSwitchRecord record);

    /**
     * 根据考试编号查询所有学生的页面切换记录
     * @param examCode 考试编号
     * @return
     */
    @Select("select recordId,examCode,studentId,switchTime,switchType,duration,returnTime " +
            "from page_switch_record where examCode = #{examCode} " +
            "order by switchTime desc")
    List<PageSwitchRecord> findByExamCode(@Param("examCode") Integer examCode);

    /**
     * 根据考试编号和学生ID查询页面切换记录
     * @param examCode 考试编号
     * @param studentId 学生ID
     * @return
     */
    @Select("select recordId,examCode,studentId,switchTime,switchType,duration,returnTime " +
            "from page_switch_record where examCode = #{examCode} and studentId = #{studentId} " +
            "order by switchTime desc")
    List<PageSwitchRecord> findByExamCodeAndStudentId(@Param("examCode") Integer examCode, @Param("studentId") Integer studentId);

    /**
     * 根据学生ID查询所有页面切换记录
     * @param studentId 学生ID
     * @return
     */
    @Select("select recordId,examCode,studentId,switchTime,switchType,duration,returnTime " +
            "from page_switch_record where studentId = #{studentId} " +
            "order by switchTime desc")
    List<PageSwitchRecord> findByStudentId(@Param("studentId") Integer studentId);

    /**
     * 根据考试编号和学生ID更新最新一条记录的返回时间和离开时长
     * @param examCode 考试编号
     * @param studentId 学生ID
     * @param returnTime 返回时间
     * @param duration 离开时长（秒）
     * @return
     */
    @Update("update page_switch_record set returnTime = #{returnTime}, duration = #{duration} " +
            "where examCode = #{examCode} and studentId = #{studentId} " +
            "and returnTime is null " +
            "order by switchTime desc limit 1")
    int updateReturnTime(@Param("examCode") Integer examCode, 
                        @Param("studentId") Integer studentId,
                        @Param("returnTime") String returnTime,
                        @Param("duration") Integer duration);
}


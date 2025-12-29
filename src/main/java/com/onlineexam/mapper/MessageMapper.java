package com.onlineexam.mapper;

import com.onlineexam.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper {
    @Select("select id,id as temp_id,title,content,time from message order by id desc")
    @Results({
            @Result(property = "replays", column = "temp_id",many = @Many(select = "com.onlineexam.mapper.ReplayMapper.findAllById"))
    })
    List<Message> findAll();

    @Select("select id,title,content,time from message where id = #{id}")
    @Results({
            @Result(property = "replays", column = "id",many = @Many(select = "com.onlineexam.mapper.ReplayMapper.findAllById"))
    })
    Message findById(Integer id);

    @Delete("delete from message where id = #{id}")
    int delete(Integer id);

    @Update("update message set title = #{title}, content = #{content}, time = #{time} where " +
            "id = #{id}")
    int update(Message message);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into message(title, content, time) values(#{title},#{content},#{time})")
    int add(Message message);
}

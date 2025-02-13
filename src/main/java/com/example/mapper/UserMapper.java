package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    // 添加根据用户ID查询用户的方法
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);

    @Insert("INSERT INTO users(username, password, created_time, update_time) " +
            "VALUES(#{username}, #{password}, #{createdTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(User user);

    @Update("UPDATE users SET username = #{username}, password = #{password}, update_time = #{updateTime} WHERE id = #{id}")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void updateById(Long id);
}
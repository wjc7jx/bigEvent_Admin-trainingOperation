package com.win.mapper;

import com.win.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

// 用户映射器接口，用于数据库操作
@Mapper
/*
 * 提供操作用户的接口
 * findByUsername(String username)
 * add(User user)
 */
public interface UserMapper {
    
    // 根据用户名查询用户
    @Select("select * from user where username = #{username}")
    User findByUsername(String username);
    
    // 添加用户
    @Insert("insert into user(username, password, create_time, update_time) " +
            " values(#{username}, #{password}, now(), now())")
    void add(User user);
    
    // 根据用户id查询用户
    @Select("select * from user where id = #{id}")
    User findById(Integer id);
    
    // 更新用户信息
    @Update("update user set username=#{username}, nickname=#{nickname}, email=#{email}, update_time=now() where id=#{id}")
    void update(User user);
    
    // 更新用户头像
    @Update("update user set user_pic = #{avatarUrl}, update_time = now() where id = #{userId}")
    void updateAvatar(@Param("userId") Integer userId, @Param("avatarUrl") String avatarUrl);
    
    // 更新用户密码
    @Update("update user set password = #{newPwd}, update_time = now() where id = #{userId}")
    void updatePwd(@Param("userId") Integer userId, @Param("newPwd") String newPwd);
}

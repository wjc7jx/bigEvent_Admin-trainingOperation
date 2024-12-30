package com.win.service;

import com.win.pojo.User;
import com.win.pojo.PasswordUpdateDTO;

public interface UserService {
    // 注册用户
    void register(String username, String password);
    
    // 登录
    User login(String username, String password);

    // 根据用户id查询用户信息
    User findUserById(Integer id);

    // 更新用户信息
    void updateUserInfo(User user);

    // 更新用户头像
    void updateAvatar(Integer userId, String avatarUrl);

    // 更新用户密码
    void updatePwd(Integer userId, PasswordUpdateDTO passwordUpdateDTO);
}

package com.win.service.impl;

import com.win.mapper.UserMapper;
import com.win.pojo.User;
import com.win.service.UserService;
import com.win.utils.Md5Util;
import com.win.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.win.pojo.PasswordUpdateDTO;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(String username, String password) {
        // 查询用户是否已存在
        User u = userMapper.findByUsername(username);
        if (u != null) {
            throw new BusinessException("用户名已被占用");
        }

        // 用户不存在，可以注册
        User user = new User();
        user.setUsername(username);
        // 使用MD5加密密码
        user.setPassword(Md5Util.getMD5String(password));
        
        // 添加用户
        userMapper.add(user);
    }

    @Override
    public User login(String username, String password) {
        // 根据用户名查询用户
        User loginUser = userMapper.findByUsername(username);
        if (loginUser == null) {
            throw new BusinessException("用户名错误");
        }

        // 判断密码是否正确
        if (!loginUser.getPassword().equals(Md5Util.getMD5String(password))) {
            throw new BusinessException("密码错误");
        }

        return loginUser;
    }

    @Override
    public User findUserById(Integer id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    @Override
    public void updateUserInfo(User user) {
        // 检查用户是否存在
        User existUser = userMapper.findById(user.getId());
        if (existUser == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查用户名是否为空或仅包含空格
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new BusinessException("用户名不能为空，且不能只包含空格");
        }
        // 如果用户名不为空，检查是否被占用
        else {
            User userByName = userMapper.findByUsername(user.getUsername());
            if (userByName != null && !userByName.getId().equals(user.getId())) {
                throw new BusinessException("���户名已被占用");
            }
        }

        // 更新用户信息
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(Integer userId, String avatarUrl) {
        // 检查用户是否存在
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 更新头像
        userMapper.updateAvatar(userId, avatarUrl);
    }

    @Override
    public void updatePwd(Integer userId, PasswordUpdateDTO passwordUpdateDTO) {
        // 检查用户是否存在
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查原密码是否正确
        if (!user.getPassword().equals(Md5Util.getMD5String(passwordUpdateDTO.getOld_pwd()))) {
            throw new BusinessException("原密码错误");
        }
        
        // 检查新密码与确认密码是否一致
        if (!passwordUpdateDTO.getNew_pwd().equals(passwordUpdateDTO.getRe_pwd())) {
            throw new BusinessException("两次输入的新密码不一致");
        }
        
        // 更新密码
        userMapper.updatePwd(userId, Md5Util.getMD5String(passwordUpdateDTO.getNew_pwd()));
    }
}

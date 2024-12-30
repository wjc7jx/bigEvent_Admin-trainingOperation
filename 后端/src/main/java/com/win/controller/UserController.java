package com.win.controller;

import com.win.pojo.Result;
import com.win.pojo.User;
import com.win.service.UserService;
import com.win.utils.JwtUtil;
import com.win.pojo.PasswordUpdateDTO;
import com.win.pojo.AvatarUpdateDTO;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Base64;

/**
 * 用户控制器，负责处理用户相关的请求
 */
@RestController
@RequestMapping("/user") // 定义用户相关的请求路径
@Validated // 启用验证功能
public class UserController {
    /**
     * 用户服务，用于调用用户相关的业务逻辑
     */
    @Autowired
    private UserService userService; // 注入用户服务

    /**
     * 获取用户信息的接口
     * 
     * @param token 用户的JWT令牌
     * @return 用户信息
     */
    @GetMapping("/userInfo") 
    public Result<User> getUserInfo(@RequestHeader(name = "Authorization") String token) {
        // 从token中获取用户id
        Map<String, Object> claims = JwtUtil.parseToken(token);// 解析token，取出键值对声明
        Integer userId = (Integer) claims.get("id");
        // 调用service获取用户信息
        User user = userService.findUserById(userId);
        return Result.success(user); // 返回成功的结果
    }

    /**
     * 更新用户信息的接口
     * 
     * @param user   用户信息
     * @param token  用户的JWT令牌
     * @return 更新结果
     */
    @PutMapping("/update") 
    public Result updateUserInfo(@RequestBody @Validated(User.Update.class) User user, @RequestHeader(name = "Authorization") String token) {
        // 从token中获取用户id
        Map<String, Object> claims = JwtUtil.parseToken(token);
        Integer userId = (Integer) claims.get("id");
        // 设置用户id
        user.setId(userId);
        // 调用service更新用户信息
        userService.updateUserInfo(user);
        return Result.success(); // 返回成功的结果
    }

    /**
     * 用户注册的接口
     * 
     * @param username 用户名
     * @param password 密码
     * @return 注册结果
     */
    @PostMapping("/register") 
    public Result register(@Pattern(regexp = "^\\S{5,16}$", message = "用户名必须是5-16位非空字符") String username,
                         @Pattern(regexp = "^\\S{5,16}$", message = "密码必须是5-16位非空字符") String password) {
        // 调用service进行注册
        try {
            userService.register(username, password);
            return Result.success(); // 返回成功的结果
        } catch (RuntimeException e) {
            return Result.error(e.getMessage()); // 返回错误信息
        }
    }

    /**
     * 用户登录的接口
     * 
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping("/login") 
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$", message = "用户名必须是5-16位非空字符") String username,
                              @Pattern(regexp = "^\\S{5,16}$", message = "密码必须是5-16位非空字符") String password) {
        // 调用service进行登录
        try {
            User loginUser = userService.login(username, password);
            
            // 登录成功，生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            
            return Result.success(token); // 返回生成的token
        } catch (RuntimeException e) {
            return Result.error(e.getMessage()); // 返回错误信息
        }
    }

    /**
     * 更新用户头像的接口
     * 
     * @param avatarUrl 头像更新信息
     * @param token     用户的JWT令牌
     * @return 更新结果
     */
    @PatchMapping("/updateAvatar") 
    public Result updateAvatar(@RequestParam String avatarUrl, @RequestHeader(name = "Authorization") String token) {
        try {
            // 从token中获取用户id
            Map<String, Object> claims = JwtUtil.parseToken(token);
            Integer userId = (Integer) claims.get("id");

            if (avatarUrl == null || avatarUrl.isEmpty()) {
                return Result.error("头像数据不能为空");
            }

            String finalAvatarUrl;
            
            // 判断是否是base64格式的图片
            if (avatarUrl.startsWith("data:image")) {
                // 处理base64图片
                String[] parts = avatarUrl.split(",");
                String imageData = parts.length > 1 ? parts[1] : parts[0];

                // 将base64转换为字节数组
                byte[] imageBytes = Base64.getDecoder().decode(imageData);

                // 生成唯一的文件名
                String fileName = UUID.randomUUID().toString() + ".jpg";
                
                // 获取上传路径
                String uploadPath = System.getProperty("user.dir") + "/uploads/";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // 保存文件
                String filePath = uploadPath + fileName;
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.write(imageBytes);
                }

                finalAvatarUrl = "/uploads/" + fileName;
            } else {
                // 如果不是base64格式，直接使用URL
                finalAvatarUrl = avatarUrl;
            }
            
            // 更新用户头像
            userService.updateAvatar(userId, finalAvatarUrl);
            
            return Result.success(finalAvatarUrl);
        } catch (IllegalArgumentException e) {
            return Result.error("无效的图片格式");
        } catch (IOException e) {
            return Result.error("头像上传失败");
        } catch (Exception e) {
            return Result.error("更新头像失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户密码的接口
     * 
     * @param passwordUpdateDTO 密码更新信息
     * @param token             用户的JWT令牌
     * @return 更新结果
     */
    @PatchMapping("/updatePwd") 
    public Result updatePwd(@RequestBody @Validated PasswordUpdateDTO passwordUpdateDTO, 
                          @RequestHeader(name = "Authorization") String token) {
        // 从token中获取用户id
        Map<String, Object> claims = JwtUtil.parseToken(token);
        Integer userId = (Integer) claims.get("id");
        // 调用service更新密码
        userService.updatePwd(userId, passwordUpdateDTO);
        return Result.success(); // 返回成功的结果
    }
}
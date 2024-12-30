package com.win.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PasswordUpdateDTO {
    @NotEmpty(message = "原密码不能为空")
    private String old_pwd;
    
    @NotEmpty(message = "新密码不能为空")
    private String new_pwd;
    
    @NotEmpty(message = "确认密码不能为空")
    private String re_pwd;
} 
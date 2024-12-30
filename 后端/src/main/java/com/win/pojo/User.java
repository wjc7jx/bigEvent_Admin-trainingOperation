package com.win.pojo;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
public class User {
    @NotNull(groups = Update.class)
    private Integer id;//主键ID
    
    @Pattern(regexp = "^\\S{5,16}$", message = "用户名必须是5-16位非空字符", groups = Register.class)
    private String username;//用户名
    
    @NotEmpty(groups = Register.class)
    @Pattern(regexp = "^\\S{5,16}$", message = "密码必须是5-16位非空字符", groups = Register.class)
    @JsonIgnore 
    private String password;//密码
    
    @NotEmpty(groups = Update.class)
    @Pattern(regexp = "^\\S{1,10}$", message = "昵称必须是1-10位非空字符", groups = Update.class)
    private String nickname;//昵称
    
    @NotEmpty(groups = Update.class)
    @Email(groups = Update.class)
    private String email;//邮箱
    
    private String userPic;//用户头像地址
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
    
    //定义校验分组
    public interface Register {
    }
    
    public interface Update {
    }
}

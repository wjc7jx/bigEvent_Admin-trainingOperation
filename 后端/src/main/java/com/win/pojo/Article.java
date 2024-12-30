package com.win.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {
    private Integer id;
    @NotEmpty(message = "文章标题不能为空")
    @Pattern(regexp = "^\\S{1,10}$", message = "文章标题必须是1-10个字符")
    private String title;
    @NotEmpty(message = "文章内容不能为空")
    private String content;
    @NotEmpty(message = "文章封面不能为空")
    private String coverImg;
    @NotEmpty(message = "发布状态不能为空")
    @Pattern(regexp = "^(已发布|草稿)$", message = "发布状态只能是[已发布]或者[草稿]")
    private String state;
    @NotNull(message = "文章分类不能为空")
    private Integer categoryId;
    private Integer createUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}

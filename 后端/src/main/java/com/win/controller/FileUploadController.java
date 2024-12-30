package com.win.controller;

import com.win.pojo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
//import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileUploadController {
    
    @Value("${file.upload-path}")
    private String uploadPath;
    
    // 允许的文件类型
    private static final Set<String> ALLOWED_TYPES = new HashSet<>(
        Arrays.asList("image/jpeg", "image/png", "image/gif")
    );
    
    // 最大文件大小（10MB）
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @PostMapping
    public Result<String> upload(MultipartFile file) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        
        // 检查文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            return Result.error("文件大小不能超过10MB");
        }
        
        // 检查文件类型
        String contentType = file.getContentType();
        if (!ALLOWED_TYPES.contains(contentType)) {
            return Result.error("只能上传JPG、PNG、GIF格式的图片");
        }
        
        // 获取安全的文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        
        // 创建目标文件夹
        File targetDir = new File(uploadPath);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        
        try {
            // 检查路径合法性
            File targetFile = new File(targetDir, filename);
            if (!targetFile.getCanonicalPath().startsWith(new File(uploadPath).getCanonicalPath())) {
                return Result.error("非法的文件路径");
            }
            
            // 保存文件
            file.transferTo(targetFile);
            
            // 返回访问路径
            String url = "/uploads/" + filename;
            return Result.success(url);
        } catch (IOException e) {
            e.printStackTrace();
            // 使用更友好的错误日志
            // log.error("文件上传失败", e);
            return Result.error("文件上传失败，请稍后重试");
        }
    }
}
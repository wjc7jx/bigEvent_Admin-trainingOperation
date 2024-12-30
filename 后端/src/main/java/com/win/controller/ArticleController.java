package com.win.controller;

import com.win.pojo.Article;
import com.win.pojo.PageBean;
import com.win.pojo.Result;
import com.win.service.ArticleService;
import com.win.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/article")
@Validated
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Result add(@RequestBody @Valid Article article, @RequestHeader("Authorization") String token) {
        Map<String, Object> claims = JwtUtil.parseToken(token);
        Integer userId = (Integer) claims.get("id");
        article.setCreateUser(userId);
        articleService.add(article);
        return Result.success();
    }

    @GetMapping
    public Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String state,
            @RequestHeader("Authorization") String token
    ) {
        Map<String, Object> claims = JwtUtil.parseToken(token);
        Integer userId = (Integer) claims.get("id");
        PageBean<Article> pb = articleService.list(pageNum, pageSize, categoryId, state, userId);
        return Result.success(pb);
    }

    @GetMapping("/detail")
    public Result<Article> detail(Integer id) {
        Article article = articleService.findById(id);
        return Result.success(article);
    }

    @PutMapping
    public Result update(@RequestBody @Valid Article article) {
        articleService.update(article);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(Integer id) {
        articleService.delete(id);
        return Result.success();
    }
} 
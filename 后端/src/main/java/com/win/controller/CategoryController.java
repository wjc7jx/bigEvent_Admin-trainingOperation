package com.win.controller;
import com.win.pojo.Category;
import com.win.pojo.Result;
import com.win.service.CategoryService;
import com.win.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
@Validated
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result add(@RequestBody @Valid Category category, @RequestHeader("Authorization") String token) {
        Map<String, Object> claims = JwtUtil.parseToken(token);
        Integer userId = (Integer) claims.get("id");
        category.setCreateUser(userId);
        categoryService.add(category);
        return Result.success();
    }

    @GetMapping
    public Result<List<Category>> list(@RequestHeader("Authorization") String token) {
        Map<String, Object> claims = JwtUtil.parseToken(token);
        Integer userId = (Integer) claims.get("id");
        List<Category> cs = categoryService.list(userId);
        return Result.success(cs);
    }

    @GetMapping("/detail")
    public Result<Category> detail(Integer id) {
        Category c = categoryService.findById(id);
        return Result.success(c);
    }

    @PutMapping
    public Result update(@RequestBody @Valid Category category) {
        categoryService.update(category);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(Integer id) {
        categoryService.delete(id);
        return Result.success();
    }
}

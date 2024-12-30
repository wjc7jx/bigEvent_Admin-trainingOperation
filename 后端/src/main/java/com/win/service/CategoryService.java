package com.win.service;

import com.win.pojo.Category;

import java.util.List;

public interface CategoryService {
    // 新增分类
    void add(Category category);

    // 获取用户的分类列表
    List<Category> list(Integer userId);

    // 根据id查询分类信息
    Category findById(Integer id);

    // 更新分类
    void update(Category category);

    // 删除分类
    void delete(Integer id);
}

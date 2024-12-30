package com.win.service.impl;

import com.win.mapper.CategoryMapper;
import com.win.pojo.Category;
import com.win.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.win.exception.BusinessException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void add(Category category) {
        categoryMapper.add(category);
    }

    @Override
    public List<Category> list(Integer userId) {
        return categoryMapper.list(userId);
    }

    @Override
    public Category findById(Integer id) {
        return categoryMapper.findById(id);
    }

    @Override
    public void update(Category category) {
        // 检查分类是否存在
        Category c = categoryMapper.findById(category.getId());
        if (c == null) {
            throw new BusinessException("分类不存在");
        }
        categoryMapper.update(category);
    }

    @Override
    public void delete(Integer id) {
        // 检查分类是否存在
        Category c = categoryMapper.findById(id);
        if (c == null) {
            throw new BusinessException("分类不存在");
        }
        categoryMapper.delete(id);
    }
}

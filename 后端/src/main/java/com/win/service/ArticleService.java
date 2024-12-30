package com.win.service;

import com.win.pojo.Article;
import com.win.pojo.PageBean;

public interface ArticleService {
    // 新增文章
    void add(Article article);

    // 条件分页列表查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state, Integer userId);

    // 根据id查询文章
    Article findById(Integer id);

    // 更新文章
    void update(Article article);

    // 删除文章
    void delete(Integer id);
} 
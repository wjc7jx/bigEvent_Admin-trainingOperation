package com.win.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.win.mapper.ArticleMapper;
import com.win.pojo.Article;
import com.win.pojo.PageBean;
import com.win.exception.BusinessException;
import com.win.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(Article article) {
        // 添加文章
        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state, Integer userId) {
        // 1. 创建PageBean对象
        PageBean<Article> pb = new PageBean<>();
        
        // 2. 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        
        // 3. 调用mapper查询
        List<Article> as = articleMapper.list(userId, categoryId, state);
        Page<Article> p = (Page<Article>) as;
        
        // 4. 设置PageBean
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }

    @Override
    public Article findById(Integer id) {
        // 根据ID查找文章
        return articleMapper.findById(id);
    }

    @Override
    public void update(Article article) {
        // 检查文章是否存在
        Article a = articleMapper.findById(article.getId());
        if (a == null) {
            throw new BusinessException("文章不存在");
        }
        // 更新文章
        articleMapper.update(article);
    }

    @Override
    public void delete(Integer id) {
        // 检查文章是否存在
        Article a = articleMapper.findById(id);
        if (a == null) {
            throw new BusinessException("文章不存在");
        }
        // 删除文章
        articleMapper.delete(id);
    }
}
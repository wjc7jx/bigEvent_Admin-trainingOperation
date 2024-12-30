package com.win.mapper;

import com.win.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    // 新增文章
    @Insert("insert into article(title,content,cover_img,state,category_id,create_user,create_time,update_time) " +
            "values(#{title},#{content},#{coverImg},#{state},#{categoryId},#{createUser},now(),now())")
    void add(Article article);

    // 条件分页列表查询
    List<Article> list(Integer userId, Integer categoryId, String state);

    // 统计文章总数
    @Select("<script>" +
            "select count(*) from article where create_user=#{userId} " +
            "<if test='categoryId != null'> and category_id=#{categoryId} </if>" +
            "<if test='state != null'> and state=#{state} </if>" +
            "</script>")
    Long count(Integer userId, Integer categoryId, String state);

    // 根据id查询文章
    @Select("select * from article where id=#{id}")
    Article findById(Integer id);

    // 更新文章
    @Update("update article set title=#{title},content=#{content},cover_img=#{coverImg},state=#{state}," +
            "category_id=#{categoryId},update_time=now() where id=#{id}")
    void update(Article article);

    // 删除文章
    @Delete("delete from article where id=#{id}")
    void delete(Integer id);
} 
package com.duyi.blog.dao;

import com.duyi.blog.domain.Blog;
import com.duyi.orm.util.DaoProxy;
import com.duyi.orm.util.SQL;
import com.duyi.orm.util.SQLEnums;

import java.util.List;

public interface BlogDao {
    public static final BlogDao instance = (BlogDao) DaoProxy.getInstance(BlogDao.class);

    @SQL(sql = "select * from blog where id = ?;", type = SQLEnums.SELECT, resultType = Blog.class)
    public Blog queryBlogById(int id);

    @SQL(sql = "select * from blog;", type = SQLEnums.SELECT, resultType = Blog.class)
    public List<Blog> queryAllBlog();

    @SQL(sql = "select * from blog order by is_top desc limit ?, ?;", type = SQLEnums.SELECT, resultType = Blog.class)
    public List<Blog> queryBlogByPage(int offset, int size);

    @SQL(sql = "insert into blog (`title`, `author`, `content`, `ctime`, `views`, `thumbs`, `is_top`) values (?,?,?,?,?,?,?);", type = SQLEnums.INSERT, resultType = Blog.class)
    public long createBlog(String title, String author, String content, int ctime, int views, int thumbs, int isTop);

    @SQL(sql = "update blog set views = views + 1 where id = ?;", type = SQLEnums.UPDATE, resultType = Blog.class)
    public void addBlogViews(int blogId);

    @SQL(sql = "update blog set thumbs = thumbs + 1 where id = ?;", type = SQLEnums.UPDATE, resultType = Blog.class)
    public void addBlogThumbs(int blogId);

    @SQL(sql = "select count(1) from blog;", type = SQLEnums.SELECT, resultType = Long.class)
    public Long queryBlogCount();

}

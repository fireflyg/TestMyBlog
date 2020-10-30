package com.duyi.blog.service;

import com.alibaba.fastjson.JSON;
import com.duyi.blog.dao.BlogDao;
import com.duyi.blog.dao.BlogTagsMappingDao;
import com.duyi.blog.dao.TagsDao;
import com.duyi.blog.domain.Blog;
import com.duyi.blog.domain.BlogTagsMapping;
import com.duyi.blog.domain.Tags;

import java.util.ArrayList;
import java.util.List;

public class BlogService {

    public Blog queryBlogById(int blogId) {
        Blog blog = BlogDao.instance.queryBlogById(blogId);
        return blog;
    }

    public List<Blog> queryBlogByPage(int offset, int size) {
        System.out.println("aaaaaaaaaaaaaa");
        List<Blog> blogList = BlogDao.instance.queryBlogByPage(offset, size);

        System.out.println(blogList);
        return blogList;
    }

    public int createBlog(Blog blog) {
        return (int) BlogDao.instance.createBlog(
                blog.getTitle(),
                blog.getAuthor(),
                blog.getContent(),
                blog.getCtime(),
                blog.getViews(),
                blog.getThumbs(),
                blog.getIsTop()
        );
    }
    public void addBlogViews(int blogId) {
        BlogDao.instance.addBlogViews(blogId);
    }

    public void addBlogThumbs(int blogId) {
        BlogDao.instance.addBlogThumbs(blogId);
    }

    public int queryBlogCount() {
        long result = BlogDao.instance.queryBlogCount();
        return (int) result;
    }

    public List<Tags> queryTagsByBlogId(int blogId) {
        List<Tags> result = new ArrayList<>();
        List<BlogTagsMapping> blogTagsMappings = BlogTagsMappingDao.instance.queryBlogTagsMappingByBlogId(blogId);
        for (BlogTagsMapping mapping : blogTagsMappings) {
            Tags tags = TagsDao.instance.queryTagsById(mapping.getTagsId());
            result.add(tags);
        }
        return result;
    }
}

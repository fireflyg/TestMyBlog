package com.duyi.test;

import com.duyi.blog.dao.BlogDao;
import com.duyi.blog.domain.Blog;

import java.util.List;

public class Test4 {
    public static void main(String[] args) {
        List<Blog> blogList = BlogDao.instance.queryAllBlog();
        for(Blog blog : blogList){
            System.out.println(blog);
        }
    }
}

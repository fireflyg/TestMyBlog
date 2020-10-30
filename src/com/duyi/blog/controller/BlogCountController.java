package com.duyi.blog.controller;

import com.duyi.blog.service.BlogService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 博客文章的controller
 */
public class BlogCountController extends HttpServlet {

    BlogService blogService = new BlogService();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        int count = blogService.queryBlogCount();
        response.getWriter().write("" + count);
    }
}

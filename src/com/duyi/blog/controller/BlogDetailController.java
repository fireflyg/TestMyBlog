package com.duyi.blog.controller;

import com.alibaba.fastjson.JSON;
import com.duyi.blog.domain.Blog;
import com.duyi.blog.service.BlogService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BlogDetailController extends HttpServlet {

    BlogService blogService = new BlogService();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        String idStr = request.getParameter("id");
        Blog blog = blogService.queryBlogById(Integer.parseInt(idStr));
        String result = JSON.toJSONString(blog);
        response.getWriter().write(result);
        blogService.addBlogViews(Integer.parseInt(idStr));
    }

}

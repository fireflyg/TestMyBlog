package com.duyi.blog.controller;

import com.duyi.blog.service.BlogService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BlogThumbsController extends HttpServlet {

    BlogService blogService = new BlogService();
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("blogId");
        blogService.addBlogThumbs(Integer.parseInt(idStr));
    }
}

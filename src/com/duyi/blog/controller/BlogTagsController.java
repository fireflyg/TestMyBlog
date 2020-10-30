package com.duyi.blog.controller;


import com.alibaba.fastjson.JSON;
import com.duyi.blog.domain.Tags;
import com.duyi.blog.service.BlogService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BlogTagsController extends HttpServlet {

    BlogService blogService = new BlogService();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        String blogIdStr = request.getParameter("blogId");
        List<Tags> result = blogService.queryTagsByBlogId(Integer.parseInt(blogIdStr));
        response.getWriter().write(JSON.toJSONString(result));
    }
}

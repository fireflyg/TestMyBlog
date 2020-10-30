package com.duyi.blog.controller;

import com.alibaba.fastjson.JSON;
import com.duyi.blog.domain.Blog;
import com.duyi.blog.service.BlogService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 1. 导包
 * 2. 继承HttpServlet
 * 3. 重写Get方法或Post方法
 * 4. 配置Web.xml
 */
public class BlogListController extends HttpServlet {

    BlogService blogService = new BlogService();

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        String offsetStr = request.getParameter("offset");
        String sizeStr = request.getParameter("size");
        int offset = offsetStr == null ? 0 : Integer.parseInt(offsetStr);
        int size = sizeStr == null ? 3 : Integer.parseInt(sizeStr);
        List<Blog> blogList = blogService.queryBlogByPage(offset, size);
        String result = JSON.toJSONString(blogList);
        response.getWriter().write(result);
    }

}

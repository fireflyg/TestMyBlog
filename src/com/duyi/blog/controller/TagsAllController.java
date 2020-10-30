package com.duyi.blog.controller;

import com.alibaba.fastjson.JSON;
import com.duyi.blog.domain.Tags;
import com.duyi.blog.service.TagsService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TagsAllController extends HttpServlet {

    private TagsService tagsService = new TagsService();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        List<Tags> tagsList = tagsService.queryAllTags();
        String result = JSON.toJSONString(tagsList);
        response.getWriter().write(result);
    }
}

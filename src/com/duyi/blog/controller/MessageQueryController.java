package com.duyi.blog.controller;

import com.alibaba.fastjson.JSON;
import com.duyi.blog.domain.Message;
import com.duyi.blog.service.MessageService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MessageQueryController extends HttpServlet {

    MessageService messageService = new MessageService();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("blogId");
        List<Message> messageList = messageService.queryMessageByBlogId(Integer.parseInt(idStr));
        String result = JSON.toJSONString(messageList);
        response.getWriter().write(result);
    }
}

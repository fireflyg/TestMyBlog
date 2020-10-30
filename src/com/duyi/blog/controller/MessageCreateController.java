package com.duyi.blog.controller;

import com.duyi.blog.domain.Message;
import com.duyi.blog.service.MessageService;
import com.duyi.blog.util.TimeUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MessageCreateController extends HttpServlet {

    MessageService messageService = new MessageService();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String content = request.getParameter("content");
        String idStr = request.getParameter("blogId");//不同的文章我们直接传文章id（也就是大于0的，如果是留言区，默认id为-1）

        Message message = new Message();
        message.setName(name);
        message.setMail(email);
        message.setContent(content);
        message.setBlogId(Integer.parseInt(idStr));
        message.setCtime(TimeUtil.getNow());

        messageService.createMessage(message);

        response.getWriter().write("ok");
    }
}

package com.duyi.blog.service;

import com.duyi.blog.dao.MessageDao;
import com.duyi.blog.domain.Message;

import java.util.List;

public class MessageService {

    public int createMessage(Message message) {

        return (int) MessageDao.instance.createMessage(
                message.getBlogId(),
                message.getName(),
                message.getMail(),
                message.getContent(),
                message.getCtime()
        );
    }

    public List<Message> queryMessageByBlogId(int blogId) {
        return MessageDao.instance.queryMessageByBlogId(blogId);
    }
}

package com.duyi.blog.dao;

import com.duyi.blog.domain.Message;
import com.duyi.orm.util.DaoProxy;
import com.duyi.orm.util.SQL;
import com.duyi.orm.util.SQLEnums;

import java.util.List;

public interface MessageDao {

    public static final MessageDao instance = (MessageDao) DaoProxy.getInstance(MessageDao.class);

    @SQL(sql = "insert into message (`blog_id`, `name`, `mail`, `content`, `ctime`) values (?,?,?,?,?);", type = SQLEnums.INSERT, resultType = Message.class)
    public long createMessage(int blogId, String name, String mail, String content, int ctime);

    @SQL(sql = "select * from message where blog_id = ?;", type = SQLEnums.SELECT, resultType = Message.class)
    public List<Message> queryMessageByBlogId(int blogId);
}

package com.duyi.blog.dao;

import com.duyi.blog.domain.Blog;
import com.duyi.blog.domain.Tags;
import com.duyi.orm.util.DaoProxy;
import com.duyi.orm.util.SQL;
import com.duyi.orm.util.SQLEnums;

import java.util.List;

public interface TagsDao {

    public static final TagsDao instance = (TagsDao) DaoProxy.getInstance(TagsDao.class);

    @SQL(sql = "insert into tags (`tag`, `ctime`) values (?,?);", type = SQLEnums.INSERT, resultType = Tags.class)
    public long createTags(String tag, int ctime);

    @SQL(sql = "select * from tags where tag = ?;", type = SQLEnums.SELECT, resultType = Tags.class)
    public Tags queryTagsByName(String tag);

    @SQL(sql = "select * from tags where id = ?;", type = SQLEnums.SELECT, resultType = Tags.class)
    public Tags queryTagsById(int tagsId);

    @SQL(sql = "select * from tags;", type = SQLEnums.SELECT, resultType = Tags.class)
    public List<Tags> queryAllTags();

}

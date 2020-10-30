package com.duyi.blog.dao;

import com.duyi.blog.domain.BlogTagsMapping;
import com.duyi.blog.domain.Tags;
import com.duyi.orm.util.DaoProxy;
import com.duyi.orm.util.SQL;
import com.duyi.orm.util.SQLEnums;

import java.util.List;

public interface BlogTagsMappingDao {

    public static final BlogTagsMappingDao instance = (BlogTagsMappingDao) DaoProxy.getInstance(BlogTagsMappingDao.class);

    @SQL(sql = "insert into blog_tags_mapping (`blog_id`, `tags_id`) values (?,?);", type = SQLEnums.INSERT, resultType = BlogTagsMapping.class)
    public void createBlogTagsMapping(int blogId, int tagsId);

    @SQL(sql = "select * from blog_tags_mapping where blog_id = ?;", type = SQLEnums.SELECT, resultType = BlogTagsMapping.class)
    public List<BlogTagsMapping> queryBlogTagsMappingByBlogId(int blogId);

}

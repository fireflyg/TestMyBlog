package com.duyi.blog.service;

import com.duyi.blog.dao.BlogTagsMappingDao;
import com.duyi.blog.domain.BlogTagsMapping;

public class BlogTagsService {

    public void createBlogTagsMapping(BlogTagsMapping blogTagsMapping) {
        BlogTagsMappingDao.instance.createBlogTagsMapping(blogTagsMapping.getBlogId(), blogTagsMapping.getTagsId());
    }
}

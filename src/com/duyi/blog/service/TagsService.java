package com.duyi.blog.service;

import com.duyi.blog.dao.TagsDao;
import com.duyi.blog.domain.Tags;

import java.util.List;

public class TagsService {

    public Tags queryTagsByName(String tagName) {
        return TagsDao.instance.queryTagsByName(tagName);
    }

    public int createTags(Tags tags) {
        return (int) TagsDao.instance.createTags(tags.getTag(), tags.getCtime());
    }

    public List<Tags> queryAllTags() {
        return TagsDao.instance.queryAllTags();
    }
}

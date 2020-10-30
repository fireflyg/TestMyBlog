package com.duyi.blog.controller;

import com.duyi.blog.domain.Blog;
import com.duyi.blog.domain.BlogTagsMapping;
import com.duyi.blog.domain.Tags;
import com.duyi.blog.service.BlogService;
import com.duyi.blog.service.BlogTagsService;
import com.duyi.blog.service.TagsService;
import com.duyi.blog.util.TimeUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BlogCreaterController extends HttpServlet {

    private BlogService blogService = new BlogService();
    private TagsService tagsService = new TagsService();
    private BlogTagsService blogTagsService = new BlogTagsService();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        try {
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String content = request.getParameter("content");
            int isTop = Integer.parseInt(request.getParameter("isTop"));
            String tags = request.getParameter("tags");

            Blog blog = new Blog();
            blog.setTitle(title);
            blog.setAuthor(author);
            blog.setContent(content);
            blog.setCtime(TimeUtil.getNow());
            blog.setViews(0);
            blog.setThumbs(0);
            blog.setIsTop(isTop);

            int blogId = blogService.createBlog(blog);

            String[] tagsArr = tags.split(",");
            for (String tag : tagsArr) {
                Tags temp = tagsService.queryTagsByName(tag);
                int tagsId = 0;
                if (temp == null) {
                    Tags newTag = new Tags();
                    newTag.setTag(tag);
                    newTag.setCtime(TimeUtil.getNow());
                    tagsId = tagsService.createTags(newTag);
                } else {
                    tagsId = temp.getId();
                }
                BlogTagsMapping blogTagsMapping = new BlogTagsMapping();
                blogTagsMapping.setBlogId(blogId);
                blogTagsMapping.setTagsId(tagsId);
                blogTagsService.createBlogTagsMapping(blogTagsMapping);
            }

        } catch (Exception e) {

        }
    }
}

package com.ashutosh.blog.service;

import com.ashutosh.blog.entity.Comments;
import org.springframework.ui.Model;

public interface CommentService {
    public void create(int id, Comments comment);
    public void delete(int id);
    public void update(int id, Comments postComment);
    public Comments findById(int id);
}

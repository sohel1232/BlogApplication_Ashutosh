package com.ashutosh.blog.service;

import com.ashutosh.blog.entity.Post;

import java.util.List;

public interface PostService{
    public void save(Post post);

    public List<Post> findAll();

    public Post findById(int id);

    public void update(int id, String title, String content);

    public void delete(Post post);
}

package com.ashutosh.blog.service;

import com.ashutosh.blog.entity.Post;
import org.springframework.ui.Model;

import java.util.List;

public interface PostService{
    public void save(Post post, String[] tagStringArray);

    public Post findById(int id);

    public void readAll(Model theModel);

    public void delete(int id);

    public void create(Model theModel);

    public void update(int id, Model theModel);

    public void read(int id, Model theModel);

    public List<Post> getListOfTitleContentTag(String data);
}

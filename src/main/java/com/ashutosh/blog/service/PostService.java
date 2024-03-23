package com.ashutosh.blog.service;

import com.ashutosh.blog.entity.Post;
import com.ashutosh.blog.entity.Tag;
import com.ashutosh.blog.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PostService{
    public void save(Post post, String[] tagStringArray);

    public Post findById(int id);

    public void readAll(Model theModel);

    public void delete(int id);

    public void create(Model theModel);

    public void update(int id, Model theModel);

    public void read(int id, Model theModel);

    public List<Post> getListOfTitleContentTag(String data);
    public List<Post> getListOfSortedPosts(List<Post> posts, String sortBy);

    public List<Post> getListOfFilteredPosts(List<Integer> tags, List<Integer> author, LocalDate fromDate, LocalDate toDate, List<Post> postsPresent);

    public List<Post> findAll();
}

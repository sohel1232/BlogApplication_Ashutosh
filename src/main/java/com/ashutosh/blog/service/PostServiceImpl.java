package com.ashutosh.blog.service;

import com.ashutosh.blog.dao.PostRepository;
import com.ashutosh.blog.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostServiceImpl implements PostService{
    private PostRepository postRepository;
    @Autowired
    public PostServiceImpl(PostRepository thePostRepository){
        postRepository  = thePostRepository;
    }

    @Override
    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Post findById(int id) {
        return postRepository.getOne(id);
    }


    @Override
    public void update(int id, String title, String content) {
        Post post = postRepository.getOne(id);
        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }
}

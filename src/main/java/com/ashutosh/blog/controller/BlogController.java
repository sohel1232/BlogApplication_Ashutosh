package com.ashutosh.blog.controller;

import com.ashutosh.blog.entity.Post;
import com.ashutosh.blog.entity.Tag;
import com.ashutosh.blog.entity.User;
import com.ashutosh.blog.service.PostService;
import com.ashutosh.blog.service.TagService;
import com.ashutosh.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.metrics.StartupStep;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class BlogController {
    private PostService postService;
    private UserService userService;

    private TagService tagService;
    @Autowired
    public BlogController(UserService theUserService,PostService thePostService ,TagService theTagService) {
        userService =theUserService;
        postService = thePostService;
        tagService = theTagService;
    }

    
    
    @RequestMapping("/publish")
    public String publishBlog(@ModelAttribute("post") Post post, Model theModel){
        System.out.println(post);

//        User user=new User("ashutosh","ashutosh3@gmail.com","51545");
       // user.setId(2);
        User user = userService.findById(1);
        post.setExcerpt("excerpt");
        post.setAuthor(user);
        List<Tag> tags = post.getTags();
        List<Tag> alltags = tagService.findAll();
        Set<String> tagNameinDb = new HashSet<>();
        for(Tag tag :alltags){
            tagNameinDb.add(tag.getName());
        }
        post.setTags("");
        for(Tag tag : tags){
            if(!tagNameinDb.contains(tag.getName())){
                Tag newTag = new Tag(tag.getName());
                post.addtags(newTag);
            }
            else{
                String name = tag.getName();
                Tag newTag = tagService.findByName(name);
                post.addtags(newTag);
                System.out.println("already present "+name);
            }
        }
        postService.save(post);
        List<Post> posts = postService.findAll();
        theModel.addAttribute("blogs", posts);
        return "Home-Page";
    }
    

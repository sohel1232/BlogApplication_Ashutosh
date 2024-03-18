package com.ashutosh.blog.controller;

import com.ashutosh.blog.entity.Post;
import com.ashutosh.blog.service.PostService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class BlogController {
    private final PostService postService;

    @Autowired
    public BlogController(PostService thePostService ) {
        postService = thePostService;
    }
	
    
    @GetMapping("/homepage")
    public String homePage(Model theModel){
        postService.readAll(theModel);
        return "Home-Page";
    }
    @GetMapping("/newpost")
    public String createBlog(Model theModel){
        postService.create(theModel);
        return "new-post";
    }
    @RequestMapping("/publish")
    public String publishBlog(HttpServletRequest request,  @ModelAttribute("post") Post post, Model theModel){
        String tagString = request.getParameter("tag");
        String[] tagStringArray = tagString.split(",");
        postService.save(post, tagStringArray);
        return "redirect:/homepage";
    }
    @GetMapping("/showblog/{postId}")
    public String showPost(@PathVariable("postId") int id, Model theModel){
        postService.read(id, theModel);
        return "Blog-Post";
    }
    
    
    @GetMapping("/updateblog/{postId}")
    public String updatePost(@PathVariable("postId") int id, Model theModel){
        postService.update(id, theModel);
        return "new-post";
    }
    
    @GetMapping("/deleteblog/{postId}")
    public String deletePost(@PathVariable("postId") int id, Model theModel){
        postService.delete(id);
        return "redirect:/homepage";
    }
    @GetMapping("/post/search")
    public String search(@RequestParam("search") String data, Model theModel){
        List<Post> posts = postService.getListOfTitleContentTag(data);
        theModel.addAttribute("posts",posts);
        theModel.addAttribute("search", data);
        return "Home-Page";
    }

    @GetMapping("post/sort")
    public String sortAndSearch(@RequestParam("search") String data, @RequestParam("sort") String sortBy, Model theModel){
        System.out.println("search value is "+data);
        System.out.println("value of sort by is "+sortBy);
        List<Post> posts = postService.getListOfSortedPosts(data, sortBy);
        System.out.println("returned posts " +posts);
        theModel.addAttribute("posts", posts);
        theModel.addAttribute("search", data);
        return "Home-Page";
    }
}

package com.ashutosh.blog.controller;

import com.ashutosh.blog.entity.Post;
import com.ashutosh.blog.entity.Tag;
import com.ashutosh.blog.entity.User;
import com.ashutosh.blog.service.PostService;
import com.ashutosh.blog.service.TagService;
import com.ashutosh.blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



@Controller
public class BlogController {
    private final PostService postService;
    private final TagService tagService;
    private final UserService userService;

    @Autowired
    public BlogController(PostService thePostService, TagService theTagService, UserService theUserService) {
        postService = thePostService;
        tagService = theTagService;
        userService = theUserService;
    }
    @GetMapping("/homepage")
    public String homePage(Model theModel,
                           @RequestParam(value = "posts", required = false) List<Post> posts,
                           @RequestParam(value = "tagsChecked", required = false) List<Integer> tagsChecked,
                           @RequestParam(value= "authorsChecked", required = false) List<Integer> authorsChecked,
                           @RequestParam(value = "search", required = false) String data){

        if(posts != null){
            theModel.addAttribute("posts", posts);}
        else{
            postService.readAll(theModel);
        }
        if(tagsChecked != null){
            theModel.addAttribute("tagsChecked", tagsChecked);
        }
        if(authorsChecked != null){
            theModel.addAttribute("authorsChecked", authorsChecked);
        }
        if(data != null){
            theModel.addAttribute("search", data);
        }
        List<Tag> tags = tagService.findAll();
        List<User> authors = userService.findAll();
        theModel.addAttribute("tags", tags);
        theModel.addAttribute("authors", authors);
        return "Home-Page";
    }
    @GetMapping("/newpost")
    public String createBlog(Model theModel){
        postService.create(theModel);
        return "new-post";
    }
    @RequestMapping("/publish")
    public String publishBlog(HttpServletRequest request,  @ModelAttribute("post") Post post){
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
    public String deletePost(@PathVariable("postId") int id){
        postService.delete(id);
        return "redirect:/homepage";
    }
    @GetMapping("/posts")
    public String posts(@RequestParam(value = "search", required = false) String data,
                      @RequestParam(value = "authorsChecked", required=false) List<Integer> authorsChecked,
                      @RequestParam(value = "tagsChecked", required = false) List<Integer> tagsChecked,
                      @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                      @RequestParam(value = "toDate" , required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate toDate,
                      @RequestParam(value = "sort", required = false) String sortBy,
                      RedirectAttributes redirectAttributes) {
        List<Post> posts;
        if (data != null && (!data.isEmpty())) {
            posts = postService.getListOfTitleContentTag(data);
        }
        else{
            posts = postService.findAll();
        }
        if ((authorsChecked != null ) || (tagsChecked != null ) || (fromDate != null && toDate != null)) {
            posts = postService.getListOfFilteredPosts(tagsChecked, authorsChecked, fromDate, toDate, posts);
        }
        if (sortBy != null && !sortBy.isEmpty()) {
            posts = postService.getListOfSortedPosts(posts, sortBy);
        }
        redirectAttributes.addAttribute("posts", posts);
        redirectAttributes.addAttribute("tagsChecked", tagsChecked);
        redirectAttributes.addAttribute("authorsChecked", authorsChecked);
        redirectAttributes.addAttribute("search", data);
        return "redirect:/homepage";
    }
}

package com.ashutosh.blog.controller;

import com.ashutosh.blog.entity.Post;
import com.ashutosh.blog.entity.Tag;
import com.ashutosh.blog.entity.User;
import com.ashutosh.blog.service.PostService;
import com.ashutosh.blog.service.TagService;
import com.ashutosh.blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                           @RequestParam(value= "authorsChecked", required = false) List<Integer> authorsChecked){

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
    @GetMapping("/post/search")
    public String search(@RequestParam("search") String data,
                         RedirectAttributes redirectAttributes){
        List<Post> posts = postService.getListOfTitleContentTag(data);
        redirectAttributes.addAttribute("posts",posts);
//        theModel.addAttribute("search", data);
        return "redirect:/homepage";
    }

    @GetMapping("post/sort")
    public String sort(@RequestParam(value = "postId",required = false) List<Integer> postsIds,
                       @RequestParam(value = "authorsChecked", required=false) List<Integer> authorsChecked,
                       @RequestParam(value = "tagsChecked", required = false) List<Integer> tagsChecked,
                       @RequestParam("sort") String sortBy,
                       RedirectAttributes redirectAttributes){
        List<Post> posts = postService.getListOfSortedPosts(postsIds, sortBy);
        redirectAttributes.addAttribute("posts", posts);
        redirectAttributes.addAttribute("tagsChecked", tagsChecked);
        redirectAttributes.addAttribute("authorsChecked", authorsChecked);
//        theModel.addAttribute("sort", sortBy);
        return "redirect:/homepage";
    }
    @GetMapping("post/filter")
    public String filter(@RequestParam(value = "authorsChecked", required=false) List<Integer> authorsChecked,
                         @RequestParam(value = "tagsChecked", required = false) List<Integer> tagsChecked,
                         @RequestParam(value = "postId", required = false) List<Integer> postsIds,
                         RedirectAttributes redirectAttributes){
        if((authorsChecked==null || authorsChecked.isEmpty()) && (tagsChecked == null || tagsChecked.isEmpty())){
            return "redirect:/homepage";
        }
        List<Post> posts = postService.getListOfFilteredPosts(tagsChecked, authorsChecked, postsIds);
        redirectAttributes.addAttribute("posts", posts);
        redirectAttributes.addAttribute("tagsChecked", tagsChecked);
        redirectAttributes.addAttribute("authorsChecked", authorsChecked);
        return "redirect:/homepage";
    }
    
}

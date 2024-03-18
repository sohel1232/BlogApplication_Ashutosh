package com.ashutosh.blog.controller;

import com.ashutosh.blog.entity.Comments;
import com.ashutosh.blog.entity.Post;
import com.ashutosh.blog.service.CommentService;
import com.ashutosh.blog.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class CommentController {
    private CommentService commentService;
    private PostService postService;

    @Autowired
    public CommentController(CommentService theCommentService, PostService thePostService){
        commentService = theCommentService;
        postService = thePostService;
    }
    @PostMapping("/comments/{postId}/{commentId}")
    public String addComment(@PathVariable("postId") int postId, @PathVariable("commentId") int commentId, @ModelAttribute("Comment") Comments postComment){
        if(commentId != 0){
            commentService.update(commentId, postComment);
            return "redirect:/showblog/" + postId;
        }
        else {
            commentService.create(postId, postComment);
            return "redirect:/showblog/" + postId;
        }
    }
    @PostMapping("/delete-comment/{postId}/{commentId}")
    public String deleteComment(@PathVariable("commentId") int commentId, @PathVariable("postId") int postId){
        commentService.delete(commentId);
        return "redirect:/showblog/"+ postId;
    }
   

}

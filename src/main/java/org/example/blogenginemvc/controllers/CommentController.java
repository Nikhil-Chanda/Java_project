package org.example.blogenginemvc.controllers;

import java.util.Optional;

import org.example.blogenginemvc.models.*;
import org.example.blogenginemvc.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired AccountService accountService;
    @Autowired PostService postService;

    @PostMapping("/posts/{id}/comment")
    public String saveNewComment(@PathVariable Long id, @ModelAttribute Comment comment, HttpSession session) {
        comment.setId(null);
        Optional<Account> optionalAccount = accountService.findByEmail(((Account)session.getAttribute("account")).getEmail());
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalAccount.isPresent() && optionalPost.isPresent()) {
            comment.setAccount(optionalAccount.get());
            comment.setPost(optionalPost.get());
            commentService.save(comment);
            return "redirect:/posts/" + optionalPost.get().getId();
        } else {
            return "404";
        }
    }

    @GetMapping("/posts/{pid}/comment/{cid}/delete")
    public String deletePost(@PathVariable Long pid, @PathVariable Long cid, HttpSession session) {
        Optional<Comment> optionalComment = commentService.getById(cid);
        Optional<Account> optionalAccount = accountService.findByEmail(((Account)session.getAttribute("account")).getEmail());
        if (optionalComment.isPresent() && (optionalAccount.get() == optionalComment.get().getAccount() || optionalAccount.get().getAuthority() == "ADMIN")) {
            Comment comment = optionalComment.get();
            commentService.delete(comment);
        }
        return "redirect:/posts/" + pid;
    }
}

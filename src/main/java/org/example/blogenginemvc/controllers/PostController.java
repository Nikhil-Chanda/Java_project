package org.example.blogenginemvc.controllers;

import java.util.*;

import org.example.blogenginemvc.models.*;
import org.example.blogenginemvc.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired AccountService accountService;
    @Autowired CommentService commentService;

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        List<Comment> comments = commentService.getAllByPost(id);
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            Comment comment = new Comment();
            model.addAttribute("comment", comment);
            model.addAttribute("comments", comments);
            model.addAttribute("post", post);
            return "post";
        } else {
            return "404";
        }
    }

    @GetMapping("/myfeed")
    public String getMyFeed(Model model, HttpSession session) {
        List<Post> posts = postService.getAll();
        Set<Account> followingList = accountService.findByEmail(((Account)session.getAttribute("account")).getEmail()).get().getFollowings();
        List<Post> myFeed = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            if (followingList.contains(posts.get(i).getAccount())) {
                myFeed.add(posts.get(i));
            }
        }
        Collections.sort(myFeed, new PostComparator());
        model.addAttribute("myfeed", myFeed);
        return "myfeed";
    }

    @GetMapping("/posts/new")
    public String createNewPost(Model model, HttpSession session) {
        Optional<Account> optionalAccount;
        if (session.getAttribute("account") != null) {
            optionalAccount = accountService.findByEmail(((Account)session.getAttribute("account")).getEmail());
        } else {
            return "redirect:/login";
        }
        if (optionalAccount.isPresent()) {
            Post post = new Post();
            model.addAttribute("post", post);
            return "post_new";
        } else {
            return "redirect:/login";
        }
    }
    
    @PostMapping("/posts/new")
    public String saveNewPost(@ModelAttribute Post post, HttpSession session) {
        Optional<Account> optionalAccount = accountService.findByEmail(((Account)session.getAttribute("account")).getEmail());
        if (optionalAccount.isPresent()) {
            post.setAccount(optionalAccount.get());
            postService.save(post);
            return "redirect:/posts/" + post.getId();
        } else {
            return "404";
        }
    }

    @GetMapping("/posts/{id}/edit")
    public String editPost(@PathVariable Long id, Model model, HttpSession session) {
        Optional<Post> optionalPost = postService.getById(id);
        Optional<Account> optionalAccount = accountService.findByEmail(((Account)session.getAttribute("account")).getEmail());
        if (optionalPost.isPresent() && optionalAccount.get() == optionalPost.get().getAccount()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_edit";
        } else {
            return "404";
        }
    }
    
    @PostMapping("/posts/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post, HttpSession session) {
        Optional<Post> optionalPost = postService.getById(id);
        Optional<Account> optionalAccount = accountService.findByEmail(((Account)session.getAttribute("account")).getEmail());
        Post originalPost = optionalPost.get();
        if (originalPost.getTitle() == post.getTitle() && originalPost.getBody() == post.getBody()) {
            return "404";
        } else {
            post.setAccount(optionalAccount.get());
            postService.save(post);
            return "redirect:/posts/" + post.getId();
        }
    }

    @GetMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id, HttpSession session) {
        Optional<Post> optionalPost = postService.getById(id);
        Optional<Account> optionalAccount = accountService.findByEmail(((Account)session.getAttribute("account")).getEmail());
        if (optionalPost.isPresent() && (optionalAccount.get() == optionalPost.get().getAccount() || optionalAccount.get().getAuthority() == "ADMIN")) {
            Post post = optionalPost.get();
            postService.delete(post);
        }
        return "redirect:/";
    }

    class PostComparator implements Comparator<Post> {
        @Override
        public int compare(Post post1, Post post2) {
            return post2.getUpdatedAt().compareTo(post1.getUpdatedAt());
        }
    }
}
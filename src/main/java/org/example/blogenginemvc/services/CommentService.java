package org.example.blogenginemvc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.blogenginemvc.models.Comment;
import org.example.blogenginemvc.repos.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepo commentRepo;

    public List<Comment> getAllByPost(Long id) {
        List<Comment> comments = commentRepo.findAll();
        List<Comment> postComments = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getPost().getId().equals(id)) {
                postComments.add(comment);
            }
        }
        return postComments;
    }

    public Optional<Comment> getById(Long id) {
        return commentRepo.findById(id);
    }

    public Comment save(Comment comment) {
        return commentRepo.save(comment);
    }

    public void delete(Comment comment) {
        commentRepo.delete(comment);
    }
}

package org.example.blogenginemvc.services;

import org.example.blogenginemvc.models.Post;
import org.example.blogenginemvc.repos.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepo;
    @Autowired
    private EntityManager entityManager;

    public Optional<Post> getById(long id) {
        return postRepo.findById(id);
    }

    public List<Post> getAll() {
        return postRepo.findAll();
    }

    public Post save(Post post) {
        if (post.getId() == null) {
            post.setCreatedAt(LocalDateTime.now());
        }
        post.setUpdatedAt(LocalDateTime.now());
        return postRepo.save(post);
    }

    public void delete(Post post) {
        postRepo.delete(post);
    }

    public List<Post> searchPostsByContent(String query) {
        String searchQuery = "SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(:query)";
        TypedQuery<Post> typedQuery = entityManager.createQuery(searchQuery, Post.class);
        typedQuery.setParameter("query", "%" + query + "%");
        return typedQuery.getResultList();
    }
}
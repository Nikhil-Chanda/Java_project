package org.example.blogenginemvc.repos;

import org.example.blogenginemvc.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment,Long> {
    
}

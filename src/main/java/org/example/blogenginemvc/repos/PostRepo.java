package org.example.blogenginemvc.repos;

import org.example.blogenginemvc.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {

}
package com.post.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.post.service.models.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByNameContainingIgnoreCaseOrTitleContainingIgnoreCase(String nameQuery, String titleQuery);
}

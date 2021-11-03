package com.mcdev.javacodingtest.repository;

import com.mcdev.javacodingtest.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, String> {
}

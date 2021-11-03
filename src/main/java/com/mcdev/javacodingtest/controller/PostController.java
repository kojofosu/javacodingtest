package com.mcdev.javacodingtest.controller;


import com.mcdev.javacodingtest.config.Config;
import com.mcdev.javacodingtest.exchange.Service;
import com.mcdev.javacodingtest.model.Post;
import com.mcdev.javacodingtest.model.PostListResponse;
import com.mcdev.javacodingtest.model.PostResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Config.POST_BASE_URL)
public class PostController {
    Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    Service postService;

    /*endpoint to add new post*/
    @PostMapping
    public ResponseEntity<PostResponse> addPost(@RequestBody Post post) {
        logger.info("Received request to add new post.");
        PostResponse response = postService.addPost(post);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*endpoint to get post by title*/
    @GetMapping(value = "/{title}")
    public ResponseEntity<PostResponse> getPostByTitle(@PathVariable String title) {
        logger.info("Received request to get post with title: " + title);
        PostResponse response = postService.getPostByTitle(title);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*endpoint to get list of available posts*/
    @GetMapping
    public ResponseEntity<PostListResponse> getPosts() {
        logger.info("Received request to get list of posts.");
        PostListResponse response = postService.getPostList();
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*endpoint to update post*/
    @PutMapping
    public ResponseEntity<PostResponse> updatePost(@RequestBody Post post) {
        logger.info("Received request to update author with title: " + post.getTitle());
        PostResponse response = postService.updatePost(post);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*endpoint to delete post*/
    @DeleteMapping(value = "/{title}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable String title) {
        logger.info("Received request to delete post");
        PostResponse response = postService.deletePost(title);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*endpoint to find posts by category*/
    @GetMapping(value = "/{category}")
    public ResponseEntity<PostListResponse> getPostsByCategory(@PathVariable String category) {
        logger.info("Received request to get list of posts with category " + category);
        PostListResponse response = postService.getPostsByCategory(category.trim());
        return new ResponseEntity<>(response, response.getStatus());
    }
}

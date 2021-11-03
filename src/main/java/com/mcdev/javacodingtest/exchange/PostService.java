package com.mcdev.javacodingtest.exchange;

import com.mcdev.javacodingtest.model.Post;
import com.mcdev.javacodingtest.model.PostListResponse;
import com.mcdev.javacodingtest.model.PostResponse;
import com.mcdev.javacodingtest.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class PostService {
    Logger logger = LoggerFactory.getLogger(PostService.class);

    PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
        logger.info("Post service initialized.");
    }

    public PostResponse addPost(Post post) {
        PostResponse response = new PostResponse();

        /*performing validation*/
        if (post.getTitle().isBlank()) {
            logger.info("Title cannot be null.");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("Title cannot be null.");
        } else {
            try {
                String title = post.getTitle().trim();
                /*first checking if post we are about to add already exists.
                * I am going to do this check using the posts' title*/
                if (postRepository.existsById(title)) {// if post already exists, return error message
                    logger.info("Post with title " + title + " already exists");
                    response.setStatus(HttpStatus.CONFLICT);
                    response.setErrormessage("Post with title " + title + " already exists");
                } else {// else if post does not exist, go ahead and add new post
                    Post responsePost = postRepository.save(post); // adding new post
                    response.setData(responsePost);
                    response.setStatus(HttpStatus.CREATED);
                    logger.info("Post added successfully.");
                }
            } catch (Exception e) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                response.setErrormessage("Error occurred with message: " + e.getMessage());
                logger.error("Error occurred " + e);
            }
        }
        response.setTimestamp(new Date().toString());
        return response;
    }

    public PostResponse getPostByTitle(String title) {
        PostResponse response = new PostResponse();

        /*performing validation*/
        if (title.isBlank()) {
            logger.info("Title cannot be null.");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("Title cannot be null.");
        } else {
            try {
                String title1 = title.trim();
                /*checking if post exists. If it does, return it. else return appropriate status*/
                Optional<Post> postOptional = postRepository.findById(title1);
                if (postOptional.isPresent()) {
                    Post responsePost = postOptional.get();

                    response.setData(responsePost);
                    response.setStatus(HttpStatus.OK);
                    logger.info("Successfully added post.");
                } else {
                    logger.info("Post with title " + title1 + " does not exist.");
                    response.setStatus(HttpStatus.NOT_FOUND);
                    response.setErrormessage("Post with title " + title1 + " does not exist.");
                }
            } catch (Exception e) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                response.setErrormessage("Error occurred with message: " + e.getMessage());
                logger.error("Error occurred " + e);
            }
        }
        response.setTimestamp(new Date().toString());
        return response;
    }

    public PostListResponse getPostList() {
        PostListResponse response = new PostListResponse();

        try {
            /*fetch all posts. Returns an empty array of there are none.*/
            response.setData(postRepository.findAll());
            response.setStatus(HttpStatus.OK);
            logger.info("Successfully fetched posts.");
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setErrormessage("Error occurred with message: " + e.getMessage());
            logger.error("Error occurred " + e);
        }

        response.setTimestamp(new Date().toString());
        return response;
    }

    public PostResponse updatePost(Post post) {
        PostResponse response = new PostResponse();

        /*performing validation*/
        if (post.getTitle().isBlank()) {// if title is null
            logger.info("Title cannot be null.");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("Title cannot be null.");
        } else {
            try {
                String title = post.getTitle().trim();
                /*checking to see if post we want to update actually exists in our database*/
                Optional<Post> postOptional = postRepository.findById(title);
                if (postOptional.isPresent()) {
                    /*do update*/
                    post.setTitle(post.getTitle().trim());// trimming to remove white spacing on ends
                    Post responsePost = postRepository.save(post);
                    response.setData(responsePost);
                    response.setStatus(HttpStatus.OK);
                    logger.info("Successfully updated post with title: " + title);
                } else {
                    logger.info("Post with title " + title + " does not exist.");
                    response.setStatus(HttpStatus.NOT_FOUND);
                    response.setErrormessage("Post with title " + title + " does not exist.");
                }

            } catch (Exception e) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                response.setErrormessage("Error occurred with message: " + e.getMessage());
                logger.error("Error occurred " + e);
            }
        }

        response.setTimestamp(new Date().toString());
        return response;
    }

    public PostResponse deletePost(String title) {
        PostResponse response = new PostResponse();

        /*performing validation*/
        if (title.isBlank()) {// if title is null
            logger.info("Title cannot be null.");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("Title cannot be null.");
        } else {
            try {
                /*checking to see if post we want to delete exists in our database.*/
                Optional<Post> postOptional = postRepository.findById(title);
                if (postOptional.isPresent()) {
                    postRepository.deleteById(title);
                    response.setStatus(HttpStatus.OK);
                    logger.info("Post with title " + title + " has been deleted successfully.");
                } else {
                    logger.info("Post with title " + title + " does not exist.");
                    response.setStatus(HttpStatus.NOT_FOUND);
                    response.setErrormessage("Post with title " + title + " does not exist.");
                }
            } catch (Exception e) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                response.setErrormessage("Error occurred with message: " + e.getMessage());
                logger.error("Error occurred " + e);
            }
        }
        response.setTimestamp(new Date().toString());
        return response;
    }
}

package com.mcdev.javacodingtest.controller;

import com.mcdev.javacodingtest.config.Config;
import com.mcdev.javacodingtest.exchange.AuthorService;
import com.mcdev.javacodingtest.model.Author;
import com.mcdev.javacodingtest.model.AuthorListResponse;
import com.mcdev.javacodingtest.model.AuthorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Config.AUTHOR_BASE_URL)
public class AuthorController {
    Logger logger = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    AuthorService authorService;

    /*endpoint to add new author*/
    @PostMapping(consumes = "application/*")
    public ResponseEntity<AuthorResponse> addAuthor(@RequestBody Author author) {
        logger.info("Received request to add new author.");
        AuthorResponse response = authorService.addAuthor(author);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*endpoint to get author by id*/
    @GetMapping(value = "/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable String id) {
        logger.info("Received request to get author with id: " + id);
        AuthorResponse response = authorService.getAuthorById(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*endpoint to get list of available authors*/
    @GetMapping
    public ResponseEntity<AuthorListResponse> getAuthors() {
        logger.info("Received request to get list of available authors.");
        AuthorListResponse response = authorService.getAuthorList();
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*endpoint to update author*/
    @PutMapping
    public ResponseEntity<AuthorResponse> updateAuthor(@RequestBody Author author) {
        logger.info("Received request to update author with id " + author.getId());
        AuthorResponse response = authorService.updateAuthor(author);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*endpoint to delete an author*/
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AuthorResponse> deleteAuthor(@PathVariable String id) {
        logger.info("Received request to delete author with id: " + id);
        AuthorResponse response = authorService.deleteAuthor(id);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
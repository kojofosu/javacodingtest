package com.mcdev.javacodingtest.exchange;

import com.mcdev.javacodingtest.model.*;
import com.mcdev.javacodingtest.repository.AuthorRepository;
import com.mcdev.javacodingtest.repository.CategoryRepository;
import com.mcdev.javacodingtest.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class Service {
    Logger logger = LoggerFactory.getLogger(Service.class);

    AuthorRepository authorRepository;
    CategoryRepository categoryRepository;
    PostRepository postRepository;

    @Autowired
    public Service(AuthorRepository authorRepository, CategoryRepository categoryRepository, PostRepository postRepository) {
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        logger.info("Author service initialized.");
    }

    /*AUTHOR*/
    public AuthorResponse addAuthor(Author author) {
        AuthorResponse response = new AuthorResponse();
        /*validating email field*/
        if (!isEmailValid(author.getEmail())) { // if email is NOT valid.
            logger.info("Email is invalid.");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("Email is invalid.");
        } else if (author.getPhone().length() != 10) {// if phone number digits' length is not equal to 10
            logger.info("Phone number is invalid.");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("Phone number is invalid.");
        } else {// else add author
            try {
                /*First checking if the author we are about to add already exists in our database
                 * I am going to do this check using the email since it is unique.*/
                if (authorRepository.existsById(author.getEmail())) {// if author already exists, send error response.
                    logger.info("Author with email " + author.getEmail() + " already exists.");
                    response.setStatus(HttpStatus.CONFLICT);
                    response.setErrormessage("Author with email " + author.getEmail() + " already exists.");
                } else {// else if author does not exist, go ahead and add author
                    author.setEmail(author.getEmail().trim());
                    Author responseAuthor = authorRepository.save(author); // adding author
                    response.setData(responseAuthor);
                    response.setStatus(HttpStatus.CREATED); // using status 201 rather than 200 because it's appropriate.
                    logger.info("Author added successfully.");
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

    public AuthorResponse getAuthorByEmail(String email) {
        AuthorResponse response = new AuthorResponse();
        /*checking that email is not null*/
        if (!isEmailValid(email)) {
            logger.info("email is invalid.");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("email is invalid.");
        } else {
            try{
                Optional<Author> authorOptional = authorRepository.findById(email);
                /*checking if Author exists with that email and return appropriate status*/
                if (authorOptional.isPresent()) {
                    Author responseAuthor = authorOptional.get();

                    response.setData(responseAuthor);
                    response.setStatus(HttpStatus.OK);
                    logger.info("Successfully fetched author with email: " + email);
                } else {// else Author does not exist, return NOT_FOUND
                    logger.info("Author with email: " + email + " does not exist.");
                    response.setStatus(HttpStatus.NOT_FOUND);
                    response.setErrormessage("Author with email: " + email + " does not exist.");
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

    public AuthorListResponse getAuthorList() {
        AuthorListResponse response = new AuthorListResponse();

        try {
            /*fetch available authors. Returns an empty array if there are none.*/
            response.setData(authorRepository.findAll());
            response.setStatus(HttpStatus.OK);

            logger.info("Successfully fetched authors.");
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setErrormessage("Error occurred with message: " + e.getMessage());
            logger.error("Error occurred " + e);
        }

        response.setTimestamp(new Date().toString());
        return response;
    }

    public AuthorResponse updateAuthor(Author author) {
        AuthorResponse response = new AuthorResponse();

        /*performing validation*/
        if (!isEmailValid(author.getEmail())) { // if email is NOT valid.
            logger.info("Email is invalid.");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("Email is invalid.");
        } else if (author.getPhone().length() != 10) {// if phone number digits' length is not equal to 10
            logger.info("Phone number is invalid.");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("Phone number is invalid.");
        } else {// else update author
            try {
                /*checking to see if author we want to update exists in our database or not*/
                Optional<Author> authorOptional = authorRepository.findById(author.getEmail());
                if (authorOptional.isPresent()) {
                    /*do update*/
                    Author responseAuthor = authorRepository.save(author);
                    response.setData(responseAuthor);
                    response.setStatus(HttpStatus.OK);
                    logger.info("Successfully updated author with email: " + author.getEmail());
                } else {
                    logger.info("Author with email " + author.getEmail() + " does not exist.");
                    response.setStatus(HttpStatus.NOT_FOUND);
                    response.setErrormessage("Author with email: " + author.getEmail() + " does not exist.");
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

    public AuthorResponse deleteAuthor(String email) {
        AuthorResponse response = new AuthorResponse();

        /*performing validation*/
        if (!isEmailValid(email)) {
            logger.info("Email is invalid.");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("Email is invalid.");
        } else {
            try {
                /*checking to see if Author we want to delete exists in the database*/
                Optional<Author> authorOptional = authorRepository.findById(email);
                if (authorOptional.isPresent()) {
                    /*do delete*/
                    authorRepository.deleteById(email);
                    response.setStatus(HttpStatus.OK);
                    logger.info("Author with email " + email + " has been deleted successfully.");
                }else {
                    logger.info("Author with email: " + email + " does not exist.");
                    response.setStatus(HttpStatus.NOT_FOUND);
                    response.setErrormessage("Author with email: " + email+ " does not exist.");
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


    /*POST*/
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

    public PostListResponse getPostsByCategory(String category) {
        PostListResponse response = new PostListResponse();



        response.setTimestamp(new Date().toString());
        return response;
    }


    /*CATEGORY*/
    public CategoryResponse addCategory(Category category) {
        CategoryResponse response = new CategoryResponse();

        if (category.getName().isBlank()) {
            logger.info("Name cannot be null");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("Name cannot be null");
        } else {
            try {
                String name = category.getName().trim();
                /*first check to see if the category we are about to add already exists in the database*/
                if (categoryRepository.existsById(name)) {// if category already exists, return appropriate response
                    logger.info("Category with name " + name + " already exists.");
                    response.setStatus(HttpStatus.CONFLICT);
                    response.setErrormessage("Category with name " + name + " already exists.");
                } else {// else if category does not already exist, then go ahead and add category
                    category.setName(name);
                    Category responseCategory = categoryRepository.save(category);
                    response.setData(responseCategory);
                    response.setStatus(HttpStatus.CREATED);
                    logger.info("Category added successfully.");
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


    public CategoryResponse getCategoryByName(String name) {
        CategoryResponse response = new CategoryResponse();

        /*checking that name is not null*/
        if (name.isBlank()) {
            logger.info("Name cannot be null");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("Name cannot be null");
        } else {
            try {
                /*checking to see if Category with that name even exits*/
                Optional<Category> optionalCategory = categoryRepository.findById(name);
                if (optionalCategory.isPresent()) {
                    Category responseCategory = optionalCategory.get();

                    response.setData(responseCategory);
                    response.setStatus(HttpStatus.OK);
                    logger.info("Successfully fetched category with name : " + name);
                } else {
                    logger.info("Category with name " + name + " does not exist.");
                    response.setStatus(HttpStatus.NOT_FOUND);
                    response.setErrormessage("Category with name " + name + " does not exist.");
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

    public CategoryListResponse getCategories() {
        CategoryListResponse response = new CategoryListResponse();

        try {
            /*fetch list of categories. Returns an empty list if there are none*/
            response.setData(categoryRepository.findAll());
            response.setStatus(HttpStatus.OK);
            logger.info("Successfully fetched categories.");
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setErrormessage("Error occurred with message: " + e.getMessage());
            logger.error("Error occurred " + e);
        }

        response.setTimestamp(new Date().toString());
        return response;
    }

    public CategoryResponse updateCategory(Category category) {
        CategoryResponse response = new CategoryResponse();

        /*performing validation*/
        if (category.getName().isBlank()) {
            logger.info("Name cannot be null");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("Name cannot be null");
        } else {
            try {
                String name = category.getName().trim();
                /*check if the category we want to update exists*/
                Optional<Category> categoryOptional = categoryRepository.findById(name);
                if (categoryOptional.isPresent()) {
                    /*do update*/
                    category.setName(name);
                    Category categoryResponse = categoryRepository.save(category);

                    response.setData(categoryResponse);
                    response.setStatus(HttpStatus.OK);
                    logger.info("Successfully updated category with name " + name);
                } else {
                    logger.info("Category with name " + name + " does not exist.");
                    response.setStatus(HttpStatus.NOT_FOUND);
                    response.setErrormessage("Category with name " + name + " does not exist.");
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


    public CategoryResponse deleteCategory(String name) {
        CategoryResponse response = new CategoryResponse();

        if (name.isBlank()) {
            logger.info("Name cannot be null");
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrormessage("Name cannot be null");
        } else {
            try {
                /*checking to see if the category we want to delete even exists*/
                Optional<Category> optionalCategory = categoryRepository.findById(name);
                if (optionalCategory.isPresent()) {
                    /*do delete*/
                    categoryRepository.deleteById(name);
                    response.setStatus(HttpStatus.OK);
                    logger.info("Category with name " + name + " has been deleted successfully.");
                } else {
                    logger.info("Category with name " + name + " does not exist.");
                    response.setStatus(HttpStatus.NOT_FOUND);
                    response.setErrormessage("Category with name " + name + " does not exist.");
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

    private boolean isEmailValid(String email) {
        Pattern regex = Pattern.compile("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");
        if (!email.isBlank()) { // this checks if email is blank or contains only whitespaces
            return regex.matcher(email).matches(); // this returns true if email matches pattern, and false otherwise.
        } else {
            return false; // returns false when email is blank
        }
    }
}

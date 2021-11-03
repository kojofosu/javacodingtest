package com.mcdev.javacodingtest.exchange;

import com.mcdev.javacodingtest.model.Author;
import com.mcdev.javacodingtest.model.AuthorListResponse;
import com.mcdev.javacodingtest.model.AuthorResponse;
import com.mcdev.javacodingtest.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class AuthorService {
    Logger logger = LoggerFactory.getLogger(AuthorService.class);

    AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
        logger.info("Author service initialized.");
    }

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

    private boolean isEmailValid(String email) {
        Pattern regex = Pattern.compile("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");
        if (!email.isBlank()) { // this checks if email is blank or contains only whitespaces
            return regex.matcher(email).matches(); // this returns true if email matches pattern, and false otherwise.
        }else{
            return false; // returns false when email is blank
        }
    }
}

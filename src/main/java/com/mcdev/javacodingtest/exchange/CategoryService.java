package com.mcdev.javacodingtest.exchange;

import com.mcdev.javacodingtest.model.Category;
import com.mcdev.javacodingtest.model.CategoryListResponse;
import com.mcdev.javacodingtest.model.CategoryResponse;
import com.mcdev.javacodingtest.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class CategoryService {
    Logger logger = LoggerFactory.getLogger(CategoryService.class);

    CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


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
}

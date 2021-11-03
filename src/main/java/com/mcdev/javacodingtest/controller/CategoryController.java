package com.mcdev.javacodingtest.controller;

import com.mcdev.javacodingtest.config.Config;
import com.mcdev.javacodingtest.exchange.CategoryService;
import com.mcdev.javacodingtest.model.Category;
import com.mcdev.javacodingtest.model.CategoryListResponse;
import com.mcdev.javacodingtest.model.CategoryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Config.CATEGORY_BASE_URL)
public class CategoryController {
    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryService categoryService;

    /*endpoint to add new category*/
    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody Category category) {
        logger.info("Received request to add new category.");
        CategoryResponse response = categoryService.addCategory(category);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*endpoint to get category by name*/
    @GetMapping(value = "/{name}")
    public ResponseEntity<CategoryResponse> getCategoryByName(@PathVariable String name) {
        logger.info("Received request to get category with name : " + name);
        CategoryResponse response = categoryService.getCategoryByName(name.trim());
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*endpoint to get list of categories*/
    @GetMapping
    public ResponseEntity<CategoryListResponse> getCategories() {
        logger.info("Received request to get list of categories.");
        CategoryListResponse response = categoryService.getCategories();
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*endpoint to update category*/
    @PutMapping
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody Category category) {
        logger.info("Received request to update category with name: " + category.getName());
        CategoryResponse response = categoryService.updateCategory(category);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*endpoint to delete category*/
    @DeleteMapping(value = "/{name}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable String name) {
        logger.info("Received request to delete category with name: " + name);
        CategoryResponse response = categoryService.deleteCategory(name.trim());
        return new ResponseEntity<>(response, response.getStatus());
    }
}

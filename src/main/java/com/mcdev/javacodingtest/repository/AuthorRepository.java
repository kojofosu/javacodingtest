package com.mcdev.javacodingtest.repository;

import com.mcdev.javacodingtest.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, String> {

}

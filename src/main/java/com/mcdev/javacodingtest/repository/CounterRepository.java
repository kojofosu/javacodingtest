package com.mcdev.javacodingtest.repository;

import com.mcdev.javacodingtest.model.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<Counter, String> {
    Counter findByCount(int count);
}

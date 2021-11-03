package com.mcdev.javacodingtest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CategoryListResponse extends Response {
    private List<Category> data;
}

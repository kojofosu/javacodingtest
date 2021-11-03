package com.mcdev.javacodingtest.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryResponse extends Response {
    private Category data;
}

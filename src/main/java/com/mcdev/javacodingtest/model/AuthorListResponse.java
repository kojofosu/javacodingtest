package com.mcdev.javacodingtest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorListResponse extends Response{
    private List<Author> data;
}

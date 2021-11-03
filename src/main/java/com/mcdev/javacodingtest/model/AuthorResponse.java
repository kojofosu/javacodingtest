package com.mcdev.javacodingtest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorResponse extends Response{
    private Author data;
}

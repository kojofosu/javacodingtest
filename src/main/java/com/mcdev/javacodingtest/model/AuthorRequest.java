package com.mcdev.javacodingtest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorRequest extends Request{
    private Author data;
}

package com.mcdev.javacodingtest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse extends Response {
    private Post data;
}

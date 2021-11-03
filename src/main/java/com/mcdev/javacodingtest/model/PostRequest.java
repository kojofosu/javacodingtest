package com.mcdev.javacodingtest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest extends Request{
    private Post data;
}

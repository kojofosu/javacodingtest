package com.mcdev.javacodingtest.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class Response {
    private String timestamp  ;
    private String errormessage;
    private HttpStatus status;
}

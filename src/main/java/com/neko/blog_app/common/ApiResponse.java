package com.neko.blog_app.common;

import org.springframework.http.HttpStatus;

public interface ApiResponse {
    public HttpStatus getStatus();
    public Object getMetadata();
    public String getMessage();
}
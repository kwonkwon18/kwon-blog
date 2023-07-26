package com.kwonblog.kwonblog.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class KwonBlogException extends RuntimeException{
// 왜 추상클래스로?

    private final Map<String, String> validation = new HashMap<>();

    public KwonBlogException(String message) {
        super(message);
    }

    public KwonBlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatus();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName,message);
    }
}

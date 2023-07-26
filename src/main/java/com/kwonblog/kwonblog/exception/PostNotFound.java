package com.kwonblog.kwonblog.exception;

public class PostNotFound extends KwonBlogException{ // 보통 unchecked Exception인 RuntimeException을 받아준다.

    private static final String MESSAGE = "존재하지 않는 글 입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 404;
    }
}

package com.kwonblog.kwonblog.exception;

import lombok.Getter;

@Getter
public class InvalidRequest extends KwonBlogException{

    public static final String MESSAGE = "잘못된 요청입니다.";

    public String fieldName;
    public String message;

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE); // 이거 해주지 않으면 오류 (상속 관련)
        addValidation(fieldName, message);
    }

    @Override
    public int getStatus() {
        return 400;
    }
}

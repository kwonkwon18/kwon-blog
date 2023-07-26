package com.kwonblog.kwonblog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/*
{
    "code" : "400",
    "message" : "잘못된 요청입니다",
    "validation" : {
        "title" : "값을 입력해주세요"
    }
 */

@Data
@JsonInclude(value = JsonInclude.Include.NON_EMPTY) // json이 비어있는 값은 안내려보내줌 (validation 없으면 안내보냄)
public class ErrorResponse {

    private final String code;

    private final String message;

    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }


}
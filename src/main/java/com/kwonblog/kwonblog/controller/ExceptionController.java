package com.kwonblog.kwonblog.controller;

import com.kwonblog.kwonblog.exception.InvalidRequest;
import com.kwonblog.kwonblog.exception.KwonBlogException;
import com.kwonblog.kwonblog.exception.PostNotFound;
import com.kwonblog.kwonblog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class) // MethodArgumentNotValidException에 관한 exception만 잡는다.
    @ResponseBody
    public ErrorResponse MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) { // spring 자체
        // MethodArgumentNotValidException
//            FieldError fieldError = e.getFieldError();
//            String field = fieldError.getField();
//            String message = fieldError.getDefaultMessage();
//            Map<String, String> response = new HashMap<>(); // hashMap을 안쓰고 응답 전용 클래스 (response) 를 만들어보자
//            response.put(field,message);

            ErrorResponse response = ErrorResponse.builder()
                        .code("400")
                        .message("잘못된 요청입니다")
                        .build();

            for (FieldError fieldError : e.getFieldErrors()){
                response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return response;
    }


    @ExceptionHandler(KwonBlogException.class) // PostNotFound 관한 exception만 잡는다.
    @ResponseBody
    public ResponseEntity<ErrorResponse> kwonBlogExceptionHandler(KwonBlogException e) { // custom
        int statusCode = e.getStatus();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

//        // 응답 json validation - title : 제목에 바보를 포함할 수 없습니다 ==> 추가해줘야함
//        if(e instanceof InvalidRequest) {
//            InvalidRequest invalidRequest = (InvalidRequest) e;
//            String fieldName = invalidRequest.getFieldName();
//            String message = invalidRequest.getMessage();
//            body.addValidation(fieldName, message);
//        }

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                                                        .body(body);

        return response;
    }

}
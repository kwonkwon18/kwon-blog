package com.kwonblog.kwonblog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PostEdit {


    private String title; // 권권 -> null (변경사항 없음)
    
    private String content; // 망원동 -> 우하하

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }

}

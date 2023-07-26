package com.kwonblog.kwonblog.request;

import com.kwonblog.kwonblog.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
public class PostCreate {

    @NotBlank(message = "타이틀 값이 없습니다.")
    private String title;

    @NotBlank(message = "컨텐츠 값이 없습니다.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if(title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다");
        }
    }

    // 빌더의 장점
    // 1. 가독성이 좋다
    // 2. 값 생성에 대한 유연함
    // 3. 필요한 값만 받을 수 있다
    // 4. 객체의 불변성
}

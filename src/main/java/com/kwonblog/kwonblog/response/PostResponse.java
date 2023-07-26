package com.kwonblog.kwonblog.response;

import com.kwonblog.kwonblog.domain.Post;
import lombok.Builder;
import lombok.Data;
/*
서비스 정책에 맞는 응답
 */
@Data
public class PostResponse {

    private Long id;
    private String title;
    private String content;

    // 생성자 오버로딩
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0,Math.min(title.length(), 10));
        this.content = content;
    }
}

package com.kwonblog.kwonblog.domain;

import com.kwonblog.kwonblog.request.PostEdit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // ** 빌더클래스 자체를 넘겨준다. 빌드가 다른 곳에서 이어지게...
    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }

//    public String getTitle() {
//        // 하면 안되는 코드
//        // 확장성이 없다
//        // 서비스의 정책을 절대 넣지 말자
//        return this.title.substring(0,10);
//    }

}

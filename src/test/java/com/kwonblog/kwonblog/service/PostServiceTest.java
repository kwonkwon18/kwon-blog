package com.kwonblog.kwonblog.service;

import com.kwonblog.kwonblog.domain.Post;
import com.kwonblog.kwonblog.exception.PostNotFound;
import com.kwonblog.kwonblog.repository.Postrepository;
import com.kwonblog.kwonblog.request.PostCreate;
import com.kwonblog.kwonblog.request.PostEdit;
import com.kwonblog.kwonblog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private Postrepository postrepository;

    @BeforeEach
    void clean() {
        postrepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        // when
        postService.write(postCreate);


        // then
        Assertions.assertEquals(1L, postrepository.count());
        Post post = postrepository.findAll().get(0);
        Assertions.assertEquals("제목입니다", post.getTitle());
        Assertions.assertEquals("내용입니다", post.getContent());
    }

    @Test
    @DisplayName("글 한개 조회")
    void test2() {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postrepository.save(requestPost);

        // 클라이언트 요구사항
            // json 응답에서 title 값 길이를 최대 10글자로 해주세요.

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        Assertions.assertNotNull(response);
        Post post_ = postrepository.findAll().get(0);
        Assertions.assertEquals("foo", post_.getTitle());
        Assertions.assertEquals("bar", post_.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {
        // given
        List<Post> requestPost = IntStream.range(1, 31)
                        .mapToObj(i -> Post.builder()
                                .title("권권 제목 " + i)
                                .content("망원동 " + i)
                                .build())
                                .collect(Collectors.toList());
        postrepository.saveAll(requestPost);
        // sql -> limit offset 알고있어야함

        // when
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        List<PostResponse> posts = postService.getList(pageable);

        // then
        Assertions.assertEquals(5L, posts.size());
        Assertions.assertEquals("권권 제목 30", posts.get(0).getTitle());
        Assertions.assertEquals("망원동 26", posts.get(4).getContent());

    }

    @Test
    @DisplayName("글 제목, 내용 수정")
    void test4() {
        // given
        Post requestPost = Post.builder()
                        .title("권권")
                        .content("망원동")
                        .build();

        postrepository.save(requestPost);

        PostEdit postEdit = PostEdit.builder()
                .title("kwonkwon")
                .content(null)
                .build();

        // when
        postService.edit(requestPost.getId(), postEdit);

        // then
        Post changedPost = postrepository.findById(requestPost.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 글입니다. id = " + requestPost.getId()));

        Assertions.assertEquals("kwonkwon", changedPost.getTitle());
        Assertions.assertEquals("망원동", changedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test5() {
        // given
        Post requestPost = Post.builder()
                .title("권권")
                .content("망원동")
                .build();

        postrepository.save(requestPost);

        // when
        postService.delete(requestPost.getId());

        //then
        Assertions.assertEquals(0, postrepository.count());
    }

    @Test
    @DisplayName("글 한개 조회 -- 오류 던져주기")
    void test6() {
        // given
        Post post = Post.builder()
                .title("권권")
                .content("망원동")
                .build();

        postrepository.save(post);

        // expected
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId()+1L);
        });

    }

}
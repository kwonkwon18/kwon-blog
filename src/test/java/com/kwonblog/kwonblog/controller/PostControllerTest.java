package com.kwonblog.kwonblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwonblog.kwonblog.domain.Post;
import com.kwonblog.kwonblog.repository.Postrepository;
import com.kwonblog.kwonblog.request.PostCreate;
import com.kwonblog.kwonblog.request.PostEdit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Postrepository postrepository;

    @BeforeEach
    void clear() {
        postrepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청")
    void test1() throws Exception {
        // 글 제목
        // 글 내용
        // 사용자
            // id
            // user
            // level 등등..

        // given
        PostCreate request_ = PostCreate.builder()
                .content("내용입니다")
                .title("제목입니다")
                .build();

        String request = objectMapper.writeValueAsString(request_);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andDo(MockMvcResultHandlers.print()); // 요청에 대한 요약이 나오게 된다.
    }

//    @Test
//    @DisplayName("/posts 요청시 title 값은 필수이다. ") // 데이터 검증의 영역
//    void test2() throws Exception {
//        // expected
//        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"title\": null,\"content\":\"내용입니다\"}")) // 빈 스트링과 null 모두 validation에 걸림
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.title").value("타이틀 값이 없습니다."))
//                // 응답 값에 대한 기댓값을 적어준 것
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    @DisplayName("/posts 요청시 db에 값이 저장된다 ")
    void test3() throws Exception {
        // given
        PostCreate request_ = PostCreate.builder()
                .content("내용입니다")
                .title("제목입니다")
                .build();

        String request = objectMapper.writeValueAsString(request_);

        // when ==> 이렇게 했을 때
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)) // 빈 스트링과 null 모두 validation에 걸림
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then ==> 뭐가 될까?
        Assertions.assertEquals(1L, postrepository.count());

        Post post = postrepository.findAll().get(0);
        Assertions.assertEquals("제목입니다", post.getTitle());
        Assertions.assertEquals("내용입니다", post.getContent());
    }

    @Test
    @DisplayName("글 한개 조회")
    void test4() throws Exception {
        // 조회 => 리퀘스트 바디는 내리지 않았고, 응답 바디만 받았다.
        /*
        MockHttpServletRequest:
        HTTP Method = GET
        Request URI = /posts/1
        Parameters = {}
        Headers = [Content-Type:"application/json;charset=UTF-8"]
        Body = null
        Session Attrs = {}
        */

        // given
        Post post = Post.builder()
                .title("1234567890123456671")
                .content("bar")
                .build();

        postrepository.save(post);

        // expected (when + then) => mockMVC를 통해서 실행
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)) // 빈 스트링과 null 모두 validation에 걸림
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("1234567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("bar"))
                .andDo(MockMvcResultHandlers.print());
    }

//    @Test
//    @DisplayName("글 여러개 조회")
//    void test5() throws Exception {
//        // given
//        List<Post> requestPost = IntStream.range(1, 31)
//                .mapToObj(i -> Post.builder()
//                        .title("권권 제목 " + i)
//                        .content("망원동 " + i)
//                        .build())
//                .collect(Collectors.toList());
//
//        postrepository.saveAll(requestPost);
//
//        // expected
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&sort=id,desc")
//                        .contentType(MediaType.APPLICATION_JSON)) // 빈 스트링과 null 모두 validation에 걸림
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(5)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(30))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("권권 제목 30"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("망원동 30"))
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    @DisplayName("글 수정")
    void test6() throws Exception {
        // given
        Post post = Post.builder()
                .title("권권")
                .content("망원동")
                .build();

        postrepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("kwonkwon")
                .content("우하하")
                .build();

        // expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", post.getId()) // PATCH /posts/{postId}
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))) // 빈 스트링과 null 모두 validation에 걸림
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test7() throws Exception {
        // given
        Post post = Post.builder()
                .title("권권")
                .content("망원동")
                .build();

        postrepository.save(post);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", post.getId()) //
                        .contentType(MediaType.APPLICATION_JSON)) // 빈 스트링과 null 모두 validation에 걸림
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글 작성시 제목에 바보는 포함될 수 없다.")
    void test8() throws Exception {
        // given
        PostCreate request_ = PostCreate.builder()
                .content("본문입니다.")
                .title("나는 바보입니다.")
                .build();

        String request = objectMapper.writeValueAsString(request_);

        // when ==> 이렇게 했을 때
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)) // 빈 스트링과 null 모두 validation에 걸림
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

    }
}
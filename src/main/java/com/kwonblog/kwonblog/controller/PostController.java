package com.kwonblog.kwonblog.controller;

import com.kwonblog.kwonblog.domain.Post;
import com.kwonblog.kwonblog.exception.InvalidRequest;
import com.kwonblog.kwonblog.request.PostCreate;
import com.kwonblog.kwonblog.request.PostEdit;
import com.kwonblog.kwonblog.response.PostResponse;
import com.kwonblog.kwonblog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController { // 블로그 글 관련 컨트롤러

    // http Method
    // GET, Post, Put, Patch, Delete 등등...

    // 데이터를 검증하는 이유

    // 1. client 개발자가 깜빡할 수 있다. 실수로 값을 안 보낼 수 있다.
    // 2. client 버그로 값이 누락될 수 있다.
    // 3. 외부에 나쁜 사람이 값을 임의로 자작해서 보낼 수 있다.
    // 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
    // 5. 서버개발자의 편안함을 위해서
    // ==> 데이터가 위의 과정을 거쳤을 때, 다음 작업의 안정성을 가질 수 있다.
    // controlleradvice를 통해서 개선 해볼 것


    // API 문서 생성
    // spring restDocs
    // - 운영코드에 영향 X
    // - Test케이스 실행 -> 문서를 생성해준다. ==> 테스트가 통과안되면 문서 생성이 안된다.



    private final PostService postService;


    // 글 등록
        // Post Method
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request /*, BindingResult result */) throws Exception {
//        // BindingResult result 에서 에러 종류와 필드값을 잡아온다.
//        // 현재는 body에 넣어서 리턴해줌 ==> Body = {"title":"must not be blank"}
//        // 이 작업은 반복적인 작업이 될 가능성이 높기 떄문에 버그 발생 확률도 높음 !!
//        if(result.hasErrors()) {
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            FieldError firstFieldError = fieldErrors.get(0);
//            String fieldName = firstFieldError.getField();
//            String errorMessage = firstFieldError.getDefaultMessage();
//
//            Map<String, String> error = new HashMap<>();
//            error.put(fieldName, errorMessage);
//            return error;
//        }

        request.validate();

        postService.write(request);
    }

    /*
    /posts -> 글 전체 조회 (검색 + 페이징)
    /posts/{postIId} -> 글 한개만 조회
     */

    // 단건 조회 API
    @GetMapping("/posts/{postId}")
    public PostResponse getOne(@PathVariable(name = "postId") Long id) {
        // 응답 클래스를 분리하세요 (서비스 정책에 맞게 (지금 상황 : title을 10글자만 넘겨라))

        // Request 클래스
        // Response 클래스

        return postService.get(id);
    }

    // 여러건 조회

//    @GetMapping("/posts") // by me
//    public List<PostResponse> getList() {
//        List<PostResponse> response = postService.getAll();
//        return response;
//    }

    // 글이 많은 경우에 비용이 많이 든다
    // 글이 -> 100,000,000 -> DB글 모두 조회하는 경우 -> DB가 뻗을 수 있다. 
    // DB -> 애플리케이션 서버전달 시간, 트래픽 비용들이 많이 발생할 수 있다.
    // 여러건 조회
    @GetMapping("/posts")
    public List<PostResponse> getList(@PageableDefault(size = 5) Pageable pageable) {
        return postService.getList(pageable);
    }

    // 글 수정
    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        return postService.edit(postId, request);
    }

    // 글 삭제
    @DeleteMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId) {
        postService.delete(postId);
    }

}

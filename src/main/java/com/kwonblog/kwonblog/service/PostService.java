package com.kwonblog.kwonblog.service;

import com.kwonblog.kwonblog.domain.Post;
import com.kwonblog.kwonblog.domain.PostEditor;
import com.kwonblog.kwonblog.exception.PostNotFound;
import com.kwonblog.kwonblog.repository.Postrepository;
import com.kwonblog.kwonblog.request.PostCreate;
import com.kwonblog.kwonblog.request.PostEdit;
import com.kwonblog.kwonblog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final Postrepository postrepository;

    public void write(PostCreate postCreate) {
        
        // postrepository.save(postCreate) postCreate가 entity가 아니므로 들어가지지 않음
        // ==> 일반 클래스를 entity 형태로 바꿔줘야함

        // postCreate -> entitu

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postrepository.save(post);
    }


    public PostResponse get(Long id) {

        Post post = postrepository.findById(id) // optinal 하게 반환해줌
                .orElseThrow(() -> new PostNotFound()); // 글이 없을 때


        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public List<PostResponse> getAll() {

        List<Post> post = postrepository.findAll();

        if(post == null || post.equals("")) {
            new IllegalArgumentException("저장된 게시글이 없습니다");
        }

        List<PostResponse> response = new ArrayList<>();

        for(int i = 0 ; i < post.size() ; i++){
            PostResponse postResponse = PostResponse.builder()
                    .id(post.get(i).getId())
                    .title(post.get(i).getTitle())
                    .content(post.get(i).getContent())
                    .build();

            response.add(postResponse);
        }

        return response;
    }

    public List<PostResponse> getList(Pageable pageable) {
        // pageable 객체
        // web -> page 1 -> 0
        // Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id")); // 불러올 페이지와 페이지당 불러올 자료의 갯수

        return postrepository.findAll(pageable).stream()
                .map(post -> new PostResponse(post))
                .collect(Collectors.toList());
    }


    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit) {

        Post post = postrepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디 입니다. "));

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

        PostEditor postEditor = postEditorBuilder
                .title(postEdit.getTitle() != null ? postEdit.getTitle() : post.getTitle())
                .content(postEdit.getContent() != null ? postEdit.getContent() : post.getContent())
                .build();

        post.edit(postEditor);

        return new PostResponse(post.getId(),post.getTitle(),post.getContent());
    }

    public void delete(Long id) {

        Post post = postrepository.findById(id)
                .orElseThrow(() -> new RuntimeException("없는 아이디 입니다."));

        // -> 존재하는 경우
        postrepository.delete(post);

    }
}

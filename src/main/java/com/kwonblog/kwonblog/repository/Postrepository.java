package com.kwonblog.kwonblog.repository;

import com.kwonblog.kwonblog.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface Postrepository extends JpaRepository<Post, Long> {



}

package com.inatel.s107.socialmedia.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inatel.s107.socialmedia.model.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {
}

package com.example.blog.services;

import com.example.blog.domain.entities.Post;
import com.example.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getAllDraftPosts(User user);
}

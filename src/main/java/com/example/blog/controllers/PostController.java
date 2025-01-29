package com.example.blog.controllers;

import com.example.blog.domain.dtos.PostDto;
import com.example.blog.domain.entities.Post;
import com.example.blog.domain.entities.User;
import com.example.blog.mappers.PostMapper;
import com.example.blog.services.PostService;
import com.example.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController extends BaseController{

    @Autowired
    private PostService postService;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId
            ){
        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(Map.of(
                "status", true,
                "message", "posts found",
                "data", postDtos
        ));
    }
    @GetMapping("/draft")
    public ResponseEntity<?> getDrafts(@RequestAttribute UUID userId){
        User loggedInUser = userService.getUserById(userId);
        List<Post> draftPosts = postService.getAllDraftPosts(loggedInUser);
        List<PostDto> postDto = draftPosts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(Map.of(
                "status", true,
                "message", "posts found",
                "data", postDto
        ));
    }
}

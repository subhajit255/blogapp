package com.example.blog.controllers;

import com.example.blog.domain.dtos.CreateTagRequest;
import com.example.blog.domain.dtos.TagResponse;
import com.example.blog.domain.entities.Tag;
import com.example.blog.mappers.TagMapper;
import com.example.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController extends BaseController{

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping()
    public ResponseEntity<?> getAllTags(){
        List<Tag> tags = tagService.getAllTags();
        List<TagResponse> tagResponses = tags.stream().map(tagMapper::toTagResponse).toList();
        return ResponseEntity.ok(Map.of(
                "status", true,
                "message", "tags found",
                "data", tagResponses
        ));
    }

//    @PostMapping("/add")
//    public ResponseEntity<?> createTags(@RequestBody CreateTagRequest createTagRequest){
//        List<Tag> savedTags = tagService.createTags(createTagRequest.getNames());
//        List<TagResponse> createTagResponse = savedTags.stream().map(tagMapper::toTagResponse).toList();
//        System.out.println(createTagResponse);
//        return ResponseEntity.ok(Map.of(
//                "status", true,
//                "message", "tags added",
//                "data", createTagResponse
//        ));
//    }
    @PostMapping("/add")
    public ResponseEntity<?> createTagRequest(@RequestBody Tag tag){
        Tag isTagCreated = tagService.createTag(tag);
        return ResponseEntity.ok(Map.of(
                "status", true,
                "message", "tags added",
                "data", isTagCreated
        ));
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<?> editTag(@PathVariable UUID id, @RequestBody Tag updatedTagDetails){
        Tag isTagUpdated = tagService.updateTag(id,updatedTagDetails);
        return ResponseEntity.ok(Map.of(
                "status", true,
                "message", "tags updated",
                "data", isTagUpdated
        ));
    }

}

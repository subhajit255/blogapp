package com.example.blog.services;

import com.example.blog.domain.dtos.CreateTagRequest;
import com.example.blog.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {
    List<Tag> getAllTags();
    Tag getTagById(UUID id);
    Tag createTag(Tag tag);
    Tag updateTag(UUID id, Tag UpdateTagDetails);
}

package com.example.blog.services.implementations;

import com.example.blog.domain.dtos.CreateTagRequest;
import com.example.blog.domain.entities.Tag;
import com.example.blog.domain.repositories.TagRepository;
import com.example.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Override
    public Tag getTagById(UUID id) {
        return tagRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No tag found using this id " + id)
        );
    }

    @Transactional
    @Override
    public Tag createTag(Tag tags) {
        return tagRepository.save(tags);
    }

    @Override
    public Tag updateTag(UUID id, Tag updateTagDetail) {
        Optional<Tag> isTag = tagRepository.findById(id);
        if(isTag.isEmpty()){
            throw new RuntimeException("Tag not found");
        }
        Tag existingTag = isTag.get();
        existingTag.setName(updateTagDetail.getName());
        return tagRepository.save(existingTag);
    }


}

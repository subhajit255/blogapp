package com.example.blog.services;

import com.example.blog.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<Category> categories();
    Category createCategory(Category category);
    void deleteCategory(UUID id);
}

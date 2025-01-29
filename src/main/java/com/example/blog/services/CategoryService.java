package com.example.blog.services;

import com.example.blog.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<Category> categories();
    Category getCategoryById(UUID id);
    Category createCategory(Category category);
    Category updateCategory(UUID id, Category categoryDetails);
    void deleteCategory(UUID id);
}

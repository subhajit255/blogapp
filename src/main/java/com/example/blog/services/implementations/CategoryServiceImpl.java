package com.example.blog.services.implementations;

import com.example.blog.domain.entities.Category;
import com.example.blog.domain.repositories.CategoryRepository;
import com.example.blog.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> categories() {
        return categoryRepository.getAllCategoriesWithPostCounts();
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Category not found with this id "+id)
        );
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if(categoryRepository.existsByNameIgnoreCase(category.getName())){
            throw new IllegalArgumentException("Category already exist with name "+category.getName());
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(UUID id, Category categoryDetails) {
        Optional<Category> isCategory = categoryRepository.findById(id);
        if(isCategory.isEmpty()){
            throw new RuntimeException("category not found");
        }
        Category existingCategory = isCategory.get();
        existingCategory.setName(categoryDetails.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(UUID id) {
        Optional<Category> isCategory = categoryRepository.findById(id);
        if(isCategory.isPresent()){
            if(!isCategory.get().getPosts().isEmpty()){
                throw new IllegalStateException("Category has post asscoiated with it");
            }
            categoryRepository.deleteById(id);
        }
    }
}

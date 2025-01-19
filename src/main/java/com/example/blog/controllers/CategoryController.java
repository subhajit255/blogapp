package com.example.blog.controllers;

import com.example.blog.domain.dtos.CategoryDto;
import com.example.blog.domain.dtos.CreateCategoryRequest;
import com.example.blog.domain.entities.Category;
import com.example.blog.mappers.CategoryMapper;
import com.example.blog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories(){
        List<CategoryDto> categories = categoryService.categories()
                .stream().map(categoryMapper::toDto)
                .toList();
        return ResponseEntity.ok(categories);
    }
    @PostMapping("/add")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest){
        Category categoryToCreate = categoryMapper.toEntity(createCategoryRequest);
        Category isCreated = categoryService.createCategory(categoryToCreate);
        return new ResponseEntity<>(
                categoryMapper.toDto(isCreated),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

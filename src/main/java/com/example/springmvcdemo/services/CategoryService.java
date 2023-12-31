package com.example.springmvcdemo.services;

import com.example.springmvcdemo.model.Category;
import com.example.springmvcdemo.repositories.CategoryRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Cacheable("categories")
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
}

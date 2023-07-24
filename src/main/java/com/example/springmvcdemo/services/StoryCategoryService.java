package com.example.springmvcdemo.services;

import com.example.springmvcdemo.repositories.StoryCategoryRepository;

public class StoryCategoryService {

    private final StoryCategoryRepository storyCategoryRepository;

    public StoryCategoryService(StoryCategoryRepository storyCategoryRepository) {
        this.storyCategoryRepository = storyCategoryRepository;
    }


}

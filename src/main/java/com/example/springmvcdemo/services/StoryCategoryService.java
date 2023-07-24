package com.example.springmvcdemo.services;

import com.example.springmvcdemo.model.StoryCategory;
import com.example.springmvcdemo.repositories.StoryCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoryCategoryService {

    private final StoryCategoryRepository storyCategoryRepository;

    public StoryCategoryService(StoryCategoryRepository storyCategoryRepository) {
        this.storyCategoryRepository = storyCategoryRepository;
    }

    public void saveAll(List<StoryCategory> storyCategoryList){
        List<StoryCategory> createdStoryCategoryList = storyCategoryRepository.saveAll(storyCategoryList);
        storyCategoryRepository.flush();
    }
}

package com.example.springmvcdemo.repositories.entityManager;

import com.example.springmvcdemo.model.Story;
import com.example.springmvcdemo.model.StoryWithUserDetail;

import java.util.List;

public interface StoryEmRepository {


    List<StoryWithUserDetail> findAllWithUserInfo();

    Story findStoryById(Long storyId);
}

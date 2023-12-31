package com.example.springmvcdemo.services;

import com.example.springmvcdemo.model.Story;
import com.example.springmvcdemo.model.StoryWithUserDetail;
import com.example.springmvcdemo.repositories.StoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoryService {

    private final StoryRepository storyRepository;

    public StoryService(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public Story createStory(Story story){
        Story newStory = storyRepository.save(story);
        storyRepository.flush();
        return newStory;
    }

    public Story updateStory(Story story){
        Story updatedStory = storyRepository.save(story);
        storyRepository.flush();
        return updatedStory;
    }
    public Story getStoryById(Long id){
//        Optional<Story> optionalStory = storyRepository.findById(id);
//        return optionalStory.orElse(null);

        return storyRepository.findStoryById(id);
    }

    public List<Story> getAllStoriesByUser(Long userId){
        List<Story> storyList = storyRepository.findAllByUser(userId);
        return storyList;
    }

    public List<Story> getAllStories(){
        List<Story> storyList = storyRepository.findAll();
        return storyList;
    }

    public List<StoryWithUserDetail> getAllStoriesWithUserDetail(){
//        List<StoryWithUserDetail> storyList = storyRepository.findAllWithUserDetail();

//        Result from native query
        List<StoryWithUserDetail> storyList = storyRepository.findAllWithUserInfo();
        return storyList;
    }

    public void deleteStory(Long storyId){
        storyRepository.deleteById(storyId);
        storyRepository.flush();
    }
}
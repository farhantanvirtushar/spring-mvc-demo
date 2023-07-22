package com.example.springmvcdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryWithUserDetail extends Story {

    private String firstName;
    private String lastName;

    public StoryWithUserDetail(Story story, String firstName, String lastName){
        super(story.getId(),story.getTitle(),story.getStory(),story.getCreatedAt(),story.getUserId());
        this.firstName = firstName;
        this.lastName = lastName;

    }
}

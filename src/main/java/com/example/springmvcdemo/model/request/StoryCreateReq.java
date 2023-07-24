package com.example.springmvcdemo.model.request;

import com.example.springmvcdemo.model.Category;
import com.example.springmvcdemo.model.Story;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoryCreateReq {

    private String title;
    private String storyText;
    private List<Long> categoryIds;
}

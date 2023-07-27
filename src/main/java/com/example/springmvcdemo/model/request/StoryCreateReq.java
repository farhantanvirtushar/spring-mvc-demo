package com.example.springmvcdemo.model.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoryCreateReq {

    @NotBlank(message = "Title is empty")
    @Size(min = 6, max = 24, message = "Invalid title length")
    private String title;
    @NotBlank(message = "Story is empty")
    private String storyText;
    @NotEmpty(message = "No category is selected")
    private List<Long> categoryIds;
}

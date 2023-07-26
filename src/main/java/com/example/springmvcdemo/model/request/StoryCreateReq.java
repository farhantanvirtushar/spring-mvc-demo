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

    @NotBlank(message = "Please select a title")
    @Size(min = 6, max = 24, message = "Title must be minimum 6 & maximum 24 chars long")
    private String title;
    @NotBlank(message = "Please write something")
    private String storyText;
    @NotEmpty(message = "Please select at leas one category")
    private List<Long> categoryIds;
}

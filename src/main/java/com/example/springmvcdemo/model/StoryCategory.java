package com.example.springmvcdemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STORY_CATEGORY")
public class StoryCategory {

    @Id
    @Column(name = "STORY_CATEGORY_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long storyCategoryId;

    @Column(name = "STORY_ID")
    private Long storyId;

    @Column(name = "CATEGORY_ID")
    private Long categoryId;
}

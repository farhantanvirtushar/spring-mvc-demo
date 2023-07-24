package com.example.springmvcdemo.repositories;

import com.example.springmvcdemo.model.StoryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryCategoryRepository extends JpaRepository<StoryCategory,Long> {

    @Query("SELECT sc FROM StoryCategory sc WHERE sc.storyId = :storyId")
    List<StoryCategory> findByStoryId(@Param("storyId") Long storyId);
}

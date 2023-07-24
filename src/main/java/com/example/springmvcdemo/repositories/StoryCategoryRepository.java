package com.example.springmvcdemo.repositories;

import com.example.springmvcdemo.model.StoryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryCategoryRepository extends JpaRepository<StoryCategory,Long> {
}

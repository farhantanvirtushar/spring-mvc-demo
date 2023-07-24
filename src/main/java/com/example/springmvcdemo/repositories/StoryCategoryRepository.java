package com.example.springmvcdemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryCategoryRepository extends JpaRepository<StoryCategoryRepository,Long> {
}

package com.example.springmvcdemo.repositories;

import com.example.springmvcdemo.model.Story;
import com.example.springmvcdemo.model.StoryWithUserDetail;
import com.example.springmvcdemo.repositories.entityManager.StoryEmRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StoryRepository extends JpaRepository<Story,Long>, StoryEmRepository {

    @Query("SELECT s FROM Story s WHERE s.userId = :userId")
    List<Story> findAllByUser(@Param("userId") Long userId);

    @Query("SELECT new com.example.springmvcdemo.model.StoryWithUserDetail(s,u.firstName,u.lastName)" +
            " FROM Story s join User u on s.userId = u.id")
    List<StoryWithUserDetail> findAllWithUserDetail();
}

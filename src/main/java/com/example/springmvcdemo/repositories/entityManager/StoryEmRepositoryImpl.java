package com.example.springmvcdemo.repositories.entityManager;

import com.example.springmvcdemo.model.Story;
import com.example.springmvcdemo.model.StoryWithUserDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.sql.Timestamp;
import java.util.*;

public class StoryEmRepositoryImpl implements StoryEmRepository{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<StoryWithUserDetail> findAllWithUserInfo() {

        List<StoryWithUserDetail> storyWithUserDetailList = new ArrayList<>();
        try{
            List<Object[]> resultList = entityManager
                    .createNativeQuery("SELECT s.id, s.title, s.story, s.created_at, s.user_id, u.first_name, u.last_name  FROM stories s join users u on s.USER_ID = u.ID")
                    .getResultList();

            resultList.forEach(result ->{


                StoryWithUserDetail storyWithDetail = new StoryWithUserDetail();
                storyWithDetail.setId(Long.valueOf((Integer) result[0]));
                storyWithDetail.setTitle(String.valueOf(result[1]));
                storyWithDetail.setStoryText(String.valueOf(result[2]));

                storyWithDetail.setCreatedAt((Timestamp) result[3]);
                storyWithDetail.setUserId(Long.valueOf((Integer) result[4]));
                storyWithDetail.setFirstName(String.valueOf(result[5]));
                storyWithDetail.setLastName(String.valueOf(result[6]));

                storyWithUserDetailList.add(storyWithDetail);

            });


            return storyWithUserDetailList;
        }catch (Exception e){
            e.printStackTrace();
            return storyWithUserDetailList;
        }

    }

    @Override
    public Story findStoryById(Long storyId) {
        try {
            Story story = (Story) entityManager.createNativeQuery("SELECT *  FROM stories WHERE ID =:storyId", Story.class)
                    .setParameter("storyId",storyId)
                    .getSingleResult();
            return story;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

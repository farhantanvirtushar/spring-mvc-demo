package com.example.springmvcdemo.controllers;

import com.example.springmvcdemo.DemoApplication;
import com.example.springmvcdemo.model.Story;
import com.example.springmvcdemo.model.StoryWithUserDetail;
import com.example.springmvcdemo.services.StoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StoryControllerIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StoryService storyService;

    @Test
    void getAllStoriesWithUserDetail() {

        List<StoryWithUserDetail> storyList =  this.storyService.getAllStoriesWithUserDetail();
        System.out.println(storyList);
//        assertTrue(storyList.size() == 7);

    }
}
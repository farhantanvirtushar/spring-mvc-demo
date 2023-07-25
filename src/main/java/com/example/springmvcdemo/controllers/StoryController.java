package com.example.springmvcdemo.controllers;

import com.example.springmvcdemo.model.*;
import com.example.springmvcdemo.model.request.StoryCreateReq;
import com.example.springmvcdemo.services.CategoryService;
import com.example.springmvcdemo.services.StoryCategoryService;
import com.example.springmvcdemo.services.StoryService;
import com.example.springmvcdemo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/story")
public class StoryController {

    private final UserService userService;
    private final StoryService storyService;

    private final CategoryService categoryService;

    private final StoryCategoryService storyCategoryService;

    public StoryController(UserService userService, StoryService storyService, CategoryService categoryService, StoryCategoryService storyCategoryService) {
        this.userService = userService;
        this.storyService = storyService;
        this.categoryService = categoryService;
        this.storyCategoryService = storyCategoryService;
    }

//    @RequestMapping(value = "/get-all",method = RequestMethod.GET)
//    public String index(){
//
//        return "story/index";
//    }
    @ResponseBody
    @RequestMapping(value = "/get-all",method = RequestMethod.GET)
    public List<Story> getAllStories(HttpServletRequest request, HttpServletResponse response){

        try {
            List<Story> storyList = storyService.getAllStories();

            return storyList;

        } catch (Exception e){

            return new ArrayList<>();
        }

    }

    @ResponseBody
    @RequestMapping(value = "/get-all-with-user-detail",method = RequestMethod.GET)
    public List<StoryWithUserDetail> getAllStoriesWithUserDetail(HttpServletRequest request, HttpServletResponse response){

        try {
            List<StoryWithUserDetail> storyList = storyService.getAllStoriesWithUserDetail();

            return storyList;

        } catch (Exception e){

            return new ArrayList<>();
        }

    }

    @RequestMapping(value = "/create-story",method = RequestMethod.GET)
    public String createStoryView(HttpServletRequest request, HttpServletResponse response, Model model){

        StoryCreateReq storyCreateReq = new StoryCreateReq();
        model.addAttribute("story",storyCreateReq);

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList",categoryList);

        return "story/create";

    }
    @RequestMapping(value = "/create-story",method = RequestMethod.POST)
    public String createStory(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("story") StoryCreateReq storyCreateReq){

        try {
            String userEmail = request.getUserPrincipal().getName();
            User user = userService.getUserByEmail(userEmail);
            if(user == null){
                return "redirect:/login";
            }
            System.out.println("user name : "+user.getFirstName() +" "+user.getLastName());
            Story story = new Story();
            story.setTitle(storyCreateReq.getTitle());
            story.setStoryText(storyCreateReq.getStoryText());

            Long userId = user.getId();

            story.setUserId(userId);

            Date date = new Date();
            Timestamp currentTimeStamp = new Timestamp(date.getTime());
            story.setCreatedAt(currentTimeStamp);

            Story newStory = storyService.createStory(story);

            System.out.println(storyCreateReq.getCategoryIds());

            List< StoryCategory> storyCategoryList = new ArrayList<>();
            storyCreateReq.getCategoryIds().forEach(categoryId ->{
                StoryCategory storyCategory = new StoryCategory();
                storyCategory.setCategoryId(categoryId);
                storyCategory.setStoryId(newStory.getId());
                storyCategoryList.add(storyCategory);
            });

            storyCategoryService.saveAll(storyCategoryList);

            return "redirect:/";

        } catch (Exception e){
            e.printStackTrace();
            return "redirect:/create-story";
        }

    }

    
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    public String  getStoryById(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id, Model model){

        try {
            Story story = storyService.getStoryById(id);
            model.addAttribute("story",story);
            if(story == null){
                model.addAttribute("errorMessage","No Story is found");
            }else {
                List<StoryCategory> storyCategoryList = storyCategoryService.getByStoryId(story.getId());
                List<Long> categoryIds = storyCategoryList.stream().map(StoryCategory::getCategoryId).toList();
                List<Category> categoryList = categoryService.getAllCategories();

                categoryList = categoryList.stream().filter(category -> categoryIds.contains(category.getCategoryId())).toList();

                model.addAttribute("categoryList",categoryList);
            }

        } catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
        }

        return "story/detail";

    }

    @ResponseBody
    @RequestMapping(value = "/get-by-user-id/{userId}",method = RequestMethod.GET)
    public List<Story> getAllStoriesByUserId(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") Long userId){

        try {
            List<Story> storyList = storyService.getAllStoriesByUser(userId);

            return storyList;

        } catch (Exception e){
            return new ArrayList<>();
        }

    }

//    @CacheEvict(value = "categories" , allEntries = true)
//    @Scheduled(fixedRateString = "${caching.spring.categories}")
//    public void clearCategoriesCache(){
//    }
}

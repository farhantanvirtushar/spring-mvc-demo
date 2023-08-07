package com.example.springmvcdemo.controllers;

import com.example.springmvcdemo.model.*;
import com.example.springmvcdemo.model.request.StoryCreateReq;
import com.example.springmvcdemo.model.request.StoryEditReq;
import com.example.springmvcdemo.services.CategoryService;
import com.example.springmvcdemo.services.StoryCategoryService;
import com.example.springmvcdemo.services.StoryService;
import com.example.springmvcdemo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.springmvcdemo.utils.Utils.USER_ID;

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
        model.addAttribute("isValidated",false);
        return "story/create";

    }
    @RequestMapping(value = "/create-story",method = RequestMethod.POST)
    public String createStory(HttpServletRequest request, HttpServletResponse response,
                              @Valid @ModelAttribute("story") StoryCreateReq storyCreateReq,
                              BindingResult bindingResult, Model model){


        if (bindingResult.hasErrors()) {
            model.addAttribute("story",storyCreateReq);
            model.addAttribute("categoryList",categoryService.getAllCategories());
            model.addAttribute("isValidated",true);
            System.out.println("error found : "+bindingResult.getAllErrors());
            return "story/create";
        }

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
            return "story/create";
        }

    }

    @RequestMapping(value = "/edit/{storyId}",method = RequestMethod.GET)
    public String editStoryView(HttpServletRequest request, HttpServletResponse response,
                                @PathVariable("storyId") Long storyId, Model model){

        Story story = storyService.getStoryById(storyId);
        if(story == null){
            model.addAttribute("errorMessage","Story doesn't exist");
            return "redirect:/";
        }

        Long userId = (Long) request.getSession().getAttribute(USER_ID);
        if(!Objects.equals(story.getUserId(), userId)){
            model.addAttribute("errorMessage","Unauthorized action");
            return "redirect:/";
        }

        StoryEditReq storyEditReq = new StoryEditReq();
        storyEditReq.setStoryId(story.getId());
        storyEditReq.setTitle(story.getTitle());
        storyEditReq.setStoryText(story.getStoryText());

        List<Category> categoryList = categoryService.getAllCategories();
        List<Long> storyCategoryIds = storyCategoryService.getByStoryId(storyId).stream().map(StoryCategory::getCategoryId).toList();
        storyEditReq.setCategoryIds(storyCategoryIds);

        model.addAttribute("story",storyEditReq);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("isValidated",false);
        return "story/edit";

    }
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public String editStory(HttpServletRequest request, HttpServletResponse response,
                              @Valid @ModelAttribute("story") StoryEditReq storyEditReq,
                              BindingResult bindingResult, Model model){


        if (bindingResult.hasErrors()) {
            model.addAttribute("story",storyEditReq);
            model.addAttribute("categoryList",categoryService.getAllCategories());
            model.addAttribute("isValidated",true);
            System.out.println("error found : "+bindingResult.getAllErrors());
            return "story/edit";
        }

        try {


            Long userId = (Long) request.getSession().getAttribute(USER_ID);
            if(userId == null){
                return "redirect:/login";
            }

            Story story = storyService.getStoryById(storyEditReq.getStoryId());
            if(story == null){
                return "redirect:/";
            }

            if(!Objects.equals(story.getUserId(), userId)){
                model.addAttribute("errorMessage","Unauthorized request");
                return "redirect:/";
            }
            story.setTitle(storyEditReq.getTitle());
            story.setStoryText(storyEditReq.getStoryText());


            Story updateStory = storyService.updateStory(story);


            List<StoryCategory> savedCategoryList = storyCategoryService.getByStoryId(story.getId());

            Map<Long,Boolean> savedCategoriesMap = new HashMap<>();


            savedCategoryList.forEach(savedCategory -> {
                savedCategoriesMap.put(savedCategory.getCategoryId(),Boolean.TRUE);
            });


            Map<Long,Boolean> newCategoriesMap = new HashMap<>();

            storyEditReq.getCategoryIds().forEach(newCategoryId -> {
                newCategoriesMap.put(newCategoryId,Boolean.TRUE);
            });

            List<Long> storyCategoryIdsToRemove = savedCategoryList
                    .stream()
                    .filter(storyCategory -> !newCategoriesMap.containsKey(storyCategory.getCategoryId()))
                    .map(StoryCategory::getStoryCategoryId)
                    .toList();

            storyCategoryService.deleteAll(storyCategoryIdsToRemove);

            List<Long> categoryIdsToAdd = storyEditReq.getCategoryIds()
                    .stream()
                    .filter(categoryId -> !savedCategoriesMap.containsKey(categoryId))
                    .toList();

            List<StoryCategory> newStoryCategoryList = new ArrayList<>();
            categoryIdsToAdd.forEach(categoryId ->{
                StoryCategory storyCategory = new StoryCategory();
                storyCategory.setCategoryId(categoryId);
                storyCategory.setStoryId(story.getId());
                newStoryCategoryList.add(storyCategory);
            });

            storyCategoryService.saveAll(newStoryCategoryList);

            model.addAttribute("successMessage","Updated successfully");
            return "redirect:/";

        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage",e.getMessage());
            return "redirect:/";
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
}

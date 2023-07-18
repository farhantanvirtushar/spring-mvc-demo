package com.example.springmvcdemo.controllers;

import com.example.springmvcdemo.model.Story;
import com.example.springmvcdemo.model.User;
import com.example.springmvcdemo.services.StoryService;
import com.example.springmvcdemo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class StoryController {

    private final UserService userService;
    private final StoryService storyService;

    public StoryController(UserService userService, StoryService storyService) {
        this.userService = userService;
        this.storyService = storyService;
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

    @RequestMapping(value = "/create-story",method = RequestMethod.GET)
    public String createStoryView(HttpServletRequest request, HttpServletResponse response, Model model){

        Story story = new Story();
        model.addAttribute("story",story);

        return "story/create";

    }
    @RequestMapping(value = "/create-story",method = RequestMethod.POST)
    public String createStory(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("story") Story story){

        try {
            String userEmail = request.getUserPrincipal().getName();
            User user = userService.getUserByEmail(userEmail);
            if(user == null){
                return "redirect:/login";
            }
            Long userId = user.getId();

            story.setUserId(userId);

            Date date = new Date();
            Timestamp currentTimeStamp = new Timestamp(date.getTime());
            story.setCreatedAt(currentTimeStamp);

            Story newStory = storyService.createStory(story);

            return "redirect:/";

        } catch (Exception e){
            return "redirect:/create-story";
        }

    }

    
    @RequestMapping(value = "/get-by-id/{id}",method = RequestMethod.GET)
    public String  getStoryById(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id, Model model){

        try {
            Story story = storyService.getStoryById(id);
            model.addAttribute("story",story);
            if(story == null){
                model.addAttribute("errorMessage","No Story is found");
            }

        } catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
        }

        return "story/get-by-id";

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

package com.example.springmvcdemo.controllers;

import com.example.springmvcdemo.model.Category;
import com.example.springmvcdemo.model.Story;
import com.example.springmvcdemo.model.StoryCategory;
import com.example.springmvcdemo.model.User;
import com.example.springmvcdemo.services.CategoryService;
import com.example.springmvcdemo.services.StoryCategoryService;
import com.example.springmvcdemo.services.StoryService;
import com.example.springmvcdemo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final StoryService storyService;

    private final CategoryService categoryService;

    private final StoryCategoryService storyCategoryService;


    @RequestMapping(value = "/{userId}",method = RequestMethod.GET)
    public String  profile(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") Long userId, Model model){

        try {
            User user = userService.getUserById(userId);
            if(user == null){
                model.addAttribute("errorMessage","User not found");
                return "redirect:/";
            }
            user.setPassword("");
            model.addAttribute("user",user);

        } catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
        }

        return "profile/index";

    }
    @ResponseBody
    @RequestMapping(value = "/get-all-stories/{userId}",method = RequestMethod.GET)
    public List<Story> getAllStories(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") Long userId){

        try {
            List<Story> storyList = storyService.getAllStoriesByUser(userId);

            return storyList;

        } catch (Exception e){

            return new ArrayList<>();
        }

    }
}

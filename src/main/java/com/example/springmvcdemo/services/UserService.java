package com.example.springmvcdemo.services;

import com.example.springmvcdemo.model.User;
import com.example.springmvcdemo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User getUserByEmail(String email){
        User user = userRepository.findUserByEmail(email);
        return user;
    }
    public User createUser(User user){
        User newUser = userRepository.save(user);
        userRepository.flush();
        return newUser;
    }

    public User getUserById(Long userId){
        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null);
    }
}
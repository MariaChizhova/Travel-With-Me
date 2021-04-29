package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.requests.SubscribeRequest;
import com.example.demo.services.SubscribeService;
import com.sun.istack.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubscribeController {

    private final SubscribeService subscribeService;

    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @PostMapping("/add_subscribe")
    public void addSubscribe(@NotNull SubscribeRequest subscribeRequest) {
        subscribeService.addSubscribe(subscribeRequest);
    }

    @PostMapping("/delete_subscribe")
    public void deleteSubscribe(@NotNull SubscribeRequest subscribeRequest) {
        subscribeService.deleteSubscribe(subscribeRequest);
    }

    @GetMapping("/get_followings")
    public List<User> getFollowings(Long userId) {
        return subscribeService.getFollowings(userId);
    }

    @GetMapping("/get_followers")
    public List<User> getFollowers(Long userId) {
        return subscribeService.getFollowers(userId);
    }

}

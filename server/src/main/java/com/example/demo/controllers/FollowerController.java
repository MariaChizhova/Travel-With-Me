package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.requests.SubscribeRequest;
import com.example.demo.services.FollowerService;
import com.sun.istack.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FollowerController {

    private final FollowerService followerService;

    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @PostMapping("/add_subscribe")
    public void addSubscribe(@NotNull SubscribeRequest subscribeRequest) {
        followerService.addSubscribe(subscribeRequest);
    }

    @PostMapping("/delete_subscribe")
    public void deleteSubscribe(@NotNull SubscribeRequest subscribeRequest) {
        followerService.deleteSubscribe(subscribeRequest);
    }

    @GetMapping("/get_followings")
    public List<User> getFollowings(Long userId) {
        return followerService.getFollowings(userId);
    }

    @GetMapping("/get_followers")
    public List<User> getFollowers(Long userId) {
        return followerService.getFollowers(userId);
    }

}

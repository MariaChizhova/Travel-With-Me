package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.models.User;
import server.requests.SubscribeRequest;
import server.services.SubscribeService;
import com.sun.istack.NotNull;

import java.util.List;

@RestController
public class SubscribeController {

    private final SubscribeService subscribeService;

    @Autowired
    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @PostMapping("/add_subscribe")
    public void addSubscribe(@RequestBody @NotNull SubscribeRequest subscribeRequest) {
        subscribeService.addSubscribe(subscribeRequest);
    }

    @PostMapping("/delete_subscribe")
    public void deleteSubscribe(@RequestBody @NotNull SubscribeRequest subscribeRequest) {
        subscribeService.deleteSubscribe(subscribeRequest);
    }

    @GetMapping("/get_followings/{userId}")
    public List<User> getFollowings(@PathVariable @NotNull Long userId) {
        return subscribeService.getFollowings(userId);
    }

    @GetMapping("/get_followers/{userId}")
    public List<User> getFollowers(@PathVariable @NotNull Long userId) {
        return subscribeService.getFollowers(userId);
    }

}

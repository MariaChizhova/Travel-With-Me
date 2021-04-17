package com.example.demo.controllers;

import com.example.demo.requests.UserCreateRequest;
import com.example.demo.services.UserService;
import com.sun.istack.NotNull;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get_user")
    public UserCreateRequest getUser(@RequestParam @NotNull String email) {
        return userService.getUser(email);
    }

    @GetMapping("/get_avatar")
    public byte[] getAvatar(@RequestParam @NotNull String keyName) {
        return userService.getAvatar(keyName);
    }

    @PostMapping("/add_user")
    public void addUser(@RequestBody @NotNull UserCreateRequest user) {
        userService.addUser(user);
    }

    @PostMapping("/edit_user")
    public void editUser(@RequestBody @NotNull UserCreateRequest updatedUser) {
        userService.editUser(updatedUser);
    }

    @PostMapping("/edit_avatar")
    public void editAvatar(@RequestParam @NotNull String email,
                           @RequestParam @NotNull String newAvatar) {
        userService.editAvatar(email, newAvatar);
    }

}

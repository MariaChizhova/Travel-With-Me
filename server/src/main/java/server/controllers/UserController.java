package server.controllers;

import server.models.User;
import server.requests.UserEditRequest;
import server.services.UserService;
import com.sun.istack.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get_user")
    public User getUser(@RequestParam @NotNull String email) {
        return userService.getUser(email);
    }

    @GetMapping("/get_avatar")
    public byte[] getAvatar(@RequestParam @NotNull String keyName) {
        return userService.getAvatar(keyName);
    }

    @PostMapping("/add_user")
    public void addUser(@RequestParam @NotNull String email) {
        userService.addUser(email);
    }

    @PostMapping("/edit_user")
    public void editUser(@RequestBody @NotNull UserEditRequest editedUser) {
        userService.editUser(editedUser);
    }

    @PostMapping("/edit_avatar")
    public void editAvatar(@RequestParam @NotNull Long userId,
                           @RequestParam @NotNull String newAvatar) {
        userService.editAvatar(userId, newAvatar);
    }

    @PostMapping("/inc_number_followers/{userId}")
    public void incNumberFollowers(@PathVariable @NotNull Long userId) {
        userService.incNumberFollowers(userId);
    }

    @PostMapping("/dec_number_followers/{userId}")
    public void decNumberFollowers(@PathVariable @NotNull Long userId) {
        userService.decNumberFollowers(userId);
    }

    @PostMapping("/inc_number_followings/{userId}")
    public void incNumberFollowings(@PathVariable @NotNull Long userId) {
        userService.incNumberFollowings(userId);
    }

    @PostMapping("/dec_number_followings/{userId}")
    public void decNumberFollowings(@PathVariable @NotNull Long userId) {
        userService.decNumberFollowings(userId);
    }

}

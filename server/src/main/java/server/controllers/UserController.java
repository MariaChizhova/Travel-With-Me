package server.controllers;

import server.models.User;
import server.requests.UserUpdateRequest;
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
    public void editUser(@RequestBody @NotNull UserUpdateRequest updatedUser) {
        userService.editUser(updatedUser);
    }

    @PostMapping("/edit_avatar")
    public void editAvatar(@RequestParam @NotNull Long userId,
                           @RequestParam @NotNull String newAvatar) {
        userService.editAvatar(userId, newAvatar);
    }

}

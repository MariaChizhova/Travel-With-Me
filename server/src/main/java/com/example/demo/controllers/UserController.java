package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.repo.UserRepository;
import com.example.demo.requests.UserCreateRequest;
import com.example.demo.services.StorageService;
import com.sun.istack.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final StorageService storageService;

    public UserController(UserRepository userRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    @GetMapping("/get_user")
    public UserCreateRequest getUser(@RequestParam @NotNull String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(UserCreateRequest::new).orElse(null);
    }

    @GetMapping("/get_avatar")
    public byte[] getAvatar(@RequestParam String keyName) {
        return storageService.downloadFile(keyName);
    }

    @GetMapping("/delete")
    public void delete() {
        userRepository.deleteAll();
    }

    @PostMapping("/add_user")
    public void addUser(@RequestBody @NotNull UserCreateRequest user) {
        userRepository.save(new User(user));
    }

    @PostMapping("/edit_user")
    public void editUser(@RequestBody @NotNull UserCreateRequest updatedUser) {
        Optional<User> user = userRepository.findByEmail(updatedUser.getEmail());
        user.ifPresent(value -> userRepository.save(value.setAll(updatedUser)));
    }

    @PostMapping("/edit_avatar")
    public void editAvatar(@RequestParam Long userId,
                           @RequestParam String oldNameAvatar,
                           @RequestParam String newAvatar) {
        storageService.removeFile(oldNameAvatar);
        String newNameAvatar = userId.toString() + "_" + System.currentTimeMillis();
        try {
            Path uploadFilePath = Files.createTempFile(newNameAvatar, "jpg");
            byte[] bytes = DatatypeConverter.parseBase64Binary(newAvatar);
            Files.write(uploadFilePath, bytes);
            storageService.uploadFile(newNameAvatar, uploadFilePath);
            Optional<User> user = userRepository.findById(userId);
            user.ifPresent(value -> userRepository.save(value.setAvatar(newNameAvatar)));
        } catch (IOException ignored) {}
    }

}

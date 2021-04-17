package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repo.UserRepository;
import com.example.demo.requests.UserCreateRequest;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;

    public UserService(UserRepository userRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    public UserCreateRequest getUser(@NotNull String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(UserCreateRequest::new).orElse(null);
    }

    public byte[] getAvatar(@NotNull String keyName) {
        return storageService.downloadFile(keyName);
    }

    public void addUser(@NotNull UserCreateRequest user) {
        userRepository.save(new User(user));
    }

    public void editUser(@NotNull UserCreateRequest updatedUser) {
        Optional<User> user = userRepository.findByEmail(updatedUser.getEmail());
        user.ifPresent(value -> userRepository.save(value.setAll(updatedUser)));
    }

    public void editAvatar(@NotNull String email, @NotNull String newAvatar) {
        String nameAvatar = "avatar_" + email;
        storageService.removeFile(nameAvatar);
        try {
            // add changes to amazon s3 database
            Path uploadFilePath = Files.createTempFile(nameAvatar, "jpg");
            byte[] bytes = DatatypeConverter.parseBase64Binary(newAvatar);
            Files.write(uploadFilePath, bytes);
            storageService.uploadFile(nameAvatar, uploadFilePath);

            // add changes to mysql database
            Optional<User> user = userRepository.findByEmail(email);
            user.ifPresent(value -> userRepository.save(value.setAvatar(nameAvatar)));
        } catch (IOException ignored) {}
    }

}

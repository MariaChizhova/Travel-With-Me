package server.services;

import server.models.User;
import server.repositories.UserRepository;
import server.requests.UserUpdateRequest;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;

    public UserService(UserRepository userRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    public User getUserById(@NotNull Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getUser(@NotNull String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public byte[] getAvatar(@NotNull String keyName) {
        return storageService.downloadFile(keyName);
    }

    public void addUser(@NotNull String email) {
        userRepository.save(new User(email));
    }

    public void editUser(@NotNull UserUpdateRequest updatedUser) {
        Optional<User> user = userRepository.findById(updatedUser.getId());
        user.ifPresent(value -> userRepository.save(value.setAll(updatedUser)));
    }

    public void editAvatar(@NotNull Long userId, @NotNull String newAvatar) {
        // name avatar in the amazon s3 database
        String avatarName = "avatar_" + userId;

        // add changes to amazon s3 database
        storageService.removeFile(avatarName);
        storageService.uploadFile(avatarName, newAvatar);

        // add changes to mysql database
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(value -> userRepository.save(value.setAvatar(avatarName)));
    }

    public void incNumberFollowers(@NotNull Long userId) {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(value -> userRepository.save(value.incNumberFollowers()));
    }

    public void decNumberFollowers(@NotNull Long userId) {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(value -> userRepository.save(value.decNumberFollowers()));
    }

    public void incNumberFollowing(@NotNull Long userId) {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(value -> userRepository.save(value.incNumberFollowing()));
    }

    public void decNumberFollowing(@NotNull Long userId) {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(value -> userRepository.save(value.decNumberFollowing()));
    }

}

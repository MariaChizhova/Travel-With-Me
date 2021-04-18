package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.repositories.PostRepository;
import com.example.demo.requests.PostCreateRequest;
import com.sun.istack.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostService {

    private final PostRepository postRepository;
    private final StorageService storageService;

    public PostService(PostRepository postRepository, StorageService storageService) {
        this.postRepository = postRepository;
        this.storageService = storageService;
    }

    public void addPost(@NotNull PostCreateRequest postCreateRequest) {
        // name in the amazon s3 database
        String pictureName = "post_" + postCreateRequest.getAuthorId() + "_" + System.currentTimeMillis();

        // add picture to amazon s3 database
        storageService.uploadFile(pictureName, postCreateRequest.getPicture());

        // add post to mysql database
        Post post = new Post(postCreateRequest, pictureName);
        postRepository.save(post);
    }

    public List<Post> getPosts(@NotNull Long authorId) {
        List<Post> posts = new ArrayList<>();
        for (var post : postRepository.findAllByAuthorId(authorId)) {
            posts.add(post);
        }
        return posts;
    }

    public void editDescription(@NotNull Long postId, @NotNull String newDescription) {
        Optional<Post> post = postRepository.findById(postId);
        post.ifPresent(value -> postRepository.save(value.setDescription(newDescription)));
    }

    public void editPicture(@NotNull Long postId, @NotNull Long authorId,
                            @NotNull String oldPictureName, @NotNull String newPicture) {
        // name picture in the amazon s3 database
        String newPictureName = "post_" + authorId + "_" + System.currentTimeMillis();

        // add changes to amazon s3 database
        storageService.removeFile(oldPictureName);
        storageService.uploadFile(newPictureName, newPicture);

        // add changes to mysql database
        Optional<Post> post = postRepository.findById(postId);
        post.ifPresent(value -> postRepository.save(value.setPictureName(newPictureName)));
    }

    public void incNumberLikes(@NotNull Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        post.ifPresent(value -> postRepository.save(value.incNumberLikes()));
    }

    public void decNumberLikes(@NotNull Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        post.ifPresent(value -> postRepository.save(value.decNumberLikes()));
    }

    public void deletePost(@NotNull Long id) {
        postRepository.deleteById(id);
    }


}

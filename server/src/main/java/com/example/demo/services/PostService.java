package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.repositories.PostRepository;
import com.sun.istack.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getPosts(@NotNull Long authorId) {
        List<Post> posts = new ArrayList<>();
        for (var post : postRepository.findAllByAuthorId(authorId)) {
            posts.add(post);
        }
        return posts;
    }

    public void editDescription(@NotNull Long id, @NotNull String newDescription) {
        Optional<Post> post = postRepository.findById(id);
        post.ifPresent(value -> postRepository.save(value.setDescription(newDescription)));
    }

    public void incNumberLikes(@NotNull Long id) {
        Optional<Post> post = postRepository.findById(id);
        post.ifPresent(value -> postRepository.save(value.incNumberLikes()));
    }

    public void deletePost(@NotNull Long id) {
        postRepository.deleteById(id);
    }



}

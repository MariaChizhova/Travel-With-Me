package com.example.demo.services;

import com.example.demo.models.Marker;
import com.example.demo.models.MarkerPhoto;
import com.example.demo.models.Post;
import com.example.demo.repositories.MarkerPhotoRepository;
import com.example.demo.repositories.MarkerRepository;
import com.example.demo.repositories.PostRepository;
import com.example.demo.requests.MarkerCreateRequest;
import com.example.demo.requests.PostCreateRequest;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MarkerRepository markerRepository;
    private final MarkerPhotoRepository markerPhotoRepository;
    private final StorageService storageService;

    public PostService(PostRepository postRepository, MarkerRepository markerRepository,
                       MarkerPhotoRepository markerPhotoRepository, StorageService storageService) {
        this.postRepository = postRepository;
        this.markerRepository = markerRepository;
        this.markerPhotoRepository = markerPhotoRepository;
        this.storageService = storageService;
    }

    public void addPost(@NotNull PostCreateRequest postCreateRequest) {
        // name in the amazon s3 database
        String pictureName = "post_" + postCreateRequest.getAuthorId() +
                "_" + System.currentTimeMillis();

        // add picture to amazon s3 database
        storageService.uploadFile(pictureName, postCreateRequest.getPicture());

        // add post to mysql database
        Post post = new Post(postCreateRequest, pictureName);
        postRepository.save(post);

        // add marker to mysql
        addMarkers(postCreateRequest.getMarkers(), post.getId());
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

    public void incNumberLikes(@NotNull Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        post.ifPresent(value -> postRepository.save(value.incNumberLikes()));
    }

    public void decNumberLikes(@NotNull Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        post.ifPresent(value -> postRepository.save(value.decNumberLikes()));
    }

    public void deletePost(@NotNull Long postId) {
        postRepository.deleteById(postId);
    }


    private void addMarkers(Iterable<MarkerCreateRequest> markers, Long postId) {
        int index = 0;
        for (var markerRequest : markers) {
            Marker marker = new Marker(markerRequest, postId, index);
            markerRepository.save(marker);
            for (var photo : markerRequest.getPhotos()) {
                String pictureName = marker.getId().toString() + "_" + Integer.valueOf(index).toString();  // maybe fix
                storageService.uploadFile(pictureName, photo.getPhoto());
                markerPhotoRepository.save(new MarkerPhoto(marker.getId(), pictureName));
            }
            index += 1;
        }
    }


}

package server.controllers;

import server.models.Post;
import server.requests.PostCreateRequest;
import server.services.PostService;
import com.sun.istack.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/add_post")
    public void addPost(@NotNull PostCreateRequest postCreateRequest) {
        postService.addPost(postCreateRequest);
    }

    @GetMapping("/get_posts")
    public List<Post> getPosts(@NotNull Long authorId) {
        return postService.getPosts(authorId);
    }

    @PostMapping("/edit_description")
    public void editDescription(@NotNull Long postId, @NotNull String newDescription) {
        postService.editDescription(postId, newDescription);
    }

    @PostMapping("/inc_post_number_likes")
    public void incNumberLikes(@NotNull Long postId) {
        postService.incNumberLikes(postId);
    }

    @PostMapping("/dec_post_number_likes")
    public void decNumberLikes(@NotNull Long postId) {
        postService.decNumberLikes(postId);
    }

    @PostMapping("/delete_post")
    public void deletePost(@NotNull Long postId) {
        postService.deletePost(postId);
    }

}

package server.controllers;

import org.springframework.web.bind.annotation.*;
import server.models.Post;
import server.requests.PostCreateRequest;
import server.services.PostService;
import com.sun.istack.NotNull;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/add_post")
    public void addPost(@RequestBody @NotNull PostCreateRequest postCreateRequest) {
        postService.addPost(postCreateRequest);
    }

    @GetMapping("/get_posts")
    public List<Post> getPosts(@RequestParam @NotNull Long authorId) {
        return postService.getPosts(authorId);
    }

    @PostMapping("/edit_description")
    public void editDescription(@RequestParam @NotNull Long postId,
                                @RequestParam @NotNull String newDescription) {
        postService.editDescription(postId, newDescription);
    }

    @PostMapping("/inc_post_number_likes")
    public void incNumberLikes(@RequestParam @NotNull Long postId) {
        postService.incNumberLikes(postId);
    }

    @PostMapping("/dec_post_number_likes")
    public void decNumberLikes(@RequestParam @NotNull Long postId) {
        postService.decNumberLikes(postId);
    }

    @PostMapping("/delete_post")
    public void deletePost(@RequestParam @NotNull Long postId) {
        postService.deletePost(postId);
    }

}

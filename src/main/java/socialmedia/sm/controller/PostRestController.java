package socialmedia.sm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import socialmedia.sm.client.TypicodeClient;
import socialmedia.sm.exceptions.PostNotFoundException;
import socialmedia.sm.model.Post;
import socialmedia.sm.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;
    private final TypicodeClient client;

    public PostRestController(PostService postService, TypicodeClient client) {
        this.postService = postService;
        this.client = client;
    }


    @GetMapping
    public ResponseEntity<List<Post>> getAll(boolean updated) {
        List<Post> posts;
        if (updated) {
            client.updateData();
        }
        posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/byTitle")
    public ResponseEntity<List<Post>> getAllFilteredPosts(@RequestParam String title) {
        return ResponseEntity.ok(postService.getFilteredPostsBy(title));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> delete(@PathVariable int id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (PostNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<Post> edit(@RequestBody Post post) {
        try {
            postService.editPostAndReturn(post);
            return ResponseEntity.ok(post);
        } catch (PostNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

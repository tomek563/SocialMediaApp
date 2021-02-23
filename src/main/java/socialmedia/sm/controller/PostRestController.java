package socialmedia.sm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import socialmedia.sm.client.TypicodeClient;
import socialmedia.sm.request.RequestTitle;
import socialmedia.sm.exceptions.PostNotFoundException;
import socialmedia.sm.model.Post;
import socialmedia.sm.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private PostService postService;
    private TypicodeClient client;

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

    @PostMapping("/byTitle")
    public ResponseEntity<List<Post>> getAllFiltered(@RequestBody RequestTitle titleHolder) {
        return ResponseEntity.ok(postService.getFilteredPostsBy(titleHolder.getTitle()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity.HeadersBuilder<?> delete(@PathVariable int id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.noContent();
        } catch (PostNotFoundException e) {
            return ResponseEntity.notFound();
        }
    }

    @PutMapping
    public ResponseEntity<Post> edit(@RequestBody Post post) {
        try {
            postService.editPostAndReturn(post);
            return ResponseEntity.ok(post);
        } catch (PostNotFoundException e) {
            return (ResponseEntity<Post>) ResponseEntity.notFound();
        }
    }

}

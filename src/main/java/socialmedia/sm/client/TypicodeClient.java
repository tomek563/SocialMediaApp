package socialmedia.sm.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import socialmedia.sm.model.Post;
import socialmedia.sm.repository.PostRepository;
import socialmedia.sm.service.PostService;

import javax.annotation.PostConstruct;
import java.util.*;


@Component
public class TypicodeClient {
    private final PostRepository postRepository;
    private final RestTemplate restTemplate;
    private final PostService postService;
    private final long oneDayInMiliSeconds = 86400000;
    private static final String URL = "https://jsonplaceholder.typicode.com/posts";

    public TypicodeClient(PostRepository postRepository, RestTemplate restTemplate,
                          PostService postService) {
        this.postRepository = postRepository;
        this.restTemplate = restTemplate;
        this.postService = postService;
    }

    @PostConstruct
    public void download() {
        if (postRepository.count() == 0) {
            createNewThread();
        }
    }

    @Scheduled(fixedRate = oneDayInMiliSeconds, initialDelay = oneDayInMiliSeconds)
    public void updateData() {
        updateDataBase();
    }

    private void createNewThread() {
        Thread thread = new Thread(() -> {
            ResponseEntity<List<Post>> response = restTemplate.exchange(URL, HttpMethod.GET,
                    null, new ParameterizedTypeReference<List<Post>>() {
                    });
            List<Post> posts = new ArrayList<>(Objects.requireNonNull(response.getBody()));
            postRepository.saveAll(posts);
        });
        thread.start();
    }

    public void updateDataBase() {
        ResponseEntity<List<Post>> response = restTemplate.exchange(URL, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Post>>() {
                });
        List<Post> posts = new ArrayList<>(Objects.requireNonNull(response.getBody()));
        List<Post> changedPosts = postService.getChangedPosts();
        List<Post> roboczyPosts = new ArrayList<>(posts);

        for (Post post : posts) {
            for (Post postService : postService.getPosts()) {
                roboczyPosts.removeIf(post1 -> post.equals(postService));
            }
        }
        posts.removeAll(roboczyPosts);
        posts.addAll(changedPosts);
        postRepository.saveAll(posts);
    }
}

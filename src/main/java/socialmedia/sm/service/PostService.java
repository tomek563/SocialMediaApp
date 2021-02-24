package socialmedia.sm.service;

import org.springframework.stereotype.Service;
import socialmedia.sm.exceptions.PostNotFoundException;
import socialmedia.sm.model.Post;
import socialmedia.sm.repository.PostRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    public PostRepository getPostRepository() {
        return postRepository;
    }

    public void deletePost(int idPost) {
        Post post = postRepository.findById(idPost)
                .orElseThrow(() -> new PostNotFoundException("Nie znaleziono takiego posta!"));
        postRepository.delete(post);
    }

    public Post findById(int id) {
        Optional<Post> byId = postRepository.findById(id);
        byId.orElseThrow(() -> new PostNotFoundException("Nie znaleziono takiego posta!"));
        return byId.get();
    }

    public void editPost(Post postToUpdate) {
        postToUpdate.setChanged(true);
        save(postToUpdate);
    }

    private void save(Post postToUpdate) {
        postRepository.save(postToUpdate);
    }

    public List<Post> getChangedPosts() {
        return postRepository.findAll().stream()
                .filter(Post::isChanged)
                .collect(Collectors.toList());
    }


    public Post editPostAndReturn(Post post) {
        Post found = postRepository.findById(post.getId())
                .orElseThrow(() -> new PostNotFoundException("Nie znaleziono takiego posta!"));
        found.setTitle(post.getTitle());
        found.setBody(post.getBody());
        found.setChanged(true);
        save(found);
        return found;
    }

    public List<Post> getFilteredPostsBy(String title) {
        return postRepository.findByTitle(title);
    }
}

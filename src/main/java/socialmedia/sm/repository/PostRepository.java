package socialmedia.sm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import socialmedia.sm.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(int id);
    default List<Post> findByTitle(String name) {
        return findAll().stream().filter(post -> post.getLowerCaseTitle().
                matches(".*" + name.toLowerCase() + ".*"))
                .collect(Collectors.toList());
    }
}

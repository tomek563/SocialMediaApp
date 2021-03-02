package socialmedia.sm.repository;

import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import socialmedia.sm.model.Post;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(int id);

    @Query("SELECT u FROM Post u WHERE u.title like CONCAT('%',:title,'%')")
    List<Post> findByTitle(@Param("title") String title);

}

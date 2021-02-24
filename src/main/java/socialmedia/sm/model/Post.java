package socialmedia.sm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Entity
@Table(name = "POST")
public class Post {
    @Column(name = "userId")
    private int userId;
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "title")
    @NotEmpty
    private String title;
    @NotEmpty
    @Column(name = "body")
    private String body;
    private boolean changed = false;

    public Post() {
    }

    public Post(int userId, int id, @NotEmpty String title, @NotEmpty String body, boolean changed) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
        this.changed = changed;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public String getLowerCaseTitle() {
        return getTitle().toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return userId == post.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "Post{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}

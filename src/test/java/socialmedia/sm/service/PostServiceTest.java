package socialmedia.sm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import socialmedia.sm.exceptions.PostNotFoundException;
import socialmedia.sm.model.Post;
import socialmedia.sm.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostServiceTest {
    @Mock
    PostRepository postRepository;
    List<Post> posts;
    String titleFirst = "period";
    String titleSecond = "title";
    Post post;
    Post secondPost;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        posts = preparePosts();
        post = prepareFirstPost();
        secondPost = prepareSecondPost();
    }

    @Test
    void getFilteredPostsByTitle_Throws_Exception_When_Find_NoPost() {
//        given
        when(postRepository.findAll()).thenReturn(posts);
//        when
        PostService postService = new PostService(postRepository);
//        then
        assertThrows(PostNotFoundException.class, () -> postService.getFilteredPostsBy(titleFirst));
    }

    @Test
    void getFilteredPostsByTitle_Return_List_Of_Posts_With_Title() {
//        given
        when(postRepository.findAll()).thenReturn(posts);
//        when
        PostService postService = new PostService(postRepository);
//        then
        List<Post> filteredPostsBy = postService.getFilteredPostsBy(titleSecond);
        assertThat(filteredPostsBy.size(), equalTo(3));
        assertThat(filteredPostsBy.get(1).getTitle(), equalTo("Title two"));
    }

    @Test
    void deletePost_Will_Throw_Exception_When_There_Is_No_Post() {
//        given
        post = null;
        when(postRepository.findById(anyInt())).thenReturn(Optional.ofNullable(post));
//        when
        PostService postService = new PostService(postRepository);
//        then
        assertThrows(PostNotFoundException.class, () -> postService.deletePost(anyInt()));
    }

    @Test
    void deletePost_Will_Indicate_Delete_Method() {
//        given
        when(postRepository.findById(anyInt())).thenReturn(Optional.ofNullable(post));
//        when
        PostService postService = new PostService(postRepository);
        postService.deletePost(post.getId());
//        then
        verify(postRepository).delete(any());
    }

    @Test
    void findById_Will_Throw_Exception_When_No_Post_Found() {
//        given
        post = null;
        when(postRepository.findById(anyInt())).thenReturn(Optional.ofNullable(post));
//        when
        PostService postService = new PostService(postRepository);
//        then
        assertThrows(PostNotFoundException.class, () -> postService.deletePost(anyInt()));
    }

    @Test
    void findById_Will_Find_Post_With_Proper_Id() {
//        given
        when(postRepository.findById(anyInt())).thenReturn(Optional.ofNullable(post));
//        when
        PostService postService = new PostService(postRepository);
//        then

        assertThat(postService.findById(1), equalTo(post));
    }

    @Test
    void editPost_Should_Indicate_Save_Method_And_Change_Boolean_To_True() {
//        given
//        when
        PostService postService = new PostService(postRepository);
        postService.editPost(post);
//        then
        verify(postRepository).save(post);
        assertThat(post.isChanged(), equalTo(true));
    }

    @Test
    void getChangedPosts_Should_Return_List_Of_Post_Which_Are_Changed() {
//        given
        when(postRepository.findAll()).thenReturn(posts);
//        when
        PostService postService = new PostService(postRepository);
//        then
        assertThat(postService.getChangedPosts().size(), equalTo(1));
        assertThat(postService.getChangedPosts().get(0).getId(), equalTo(4));
        assertThat(postService.getChangedPosts().get(0).getTitle(), equalTo("Title four"));
    }

    @Test
    void editPostAndReturn_Should_Edit_Post_And_Return_It() {
//        given
        when(postRepository.findById(anyInt())).thenReturn(Optional.ofNullable(post));
//        when
        PostService postService = new PostService(postRepository);
        postService.editPostAndReturn(secondPost);
//        then
        assertThat(post.getTitle(), equalTo(secondPost.getTitle()));
        assertThat(post.getBody(), equalTo(secondPost.getBody()));
        verify(postRepository).save(secondPost);
    }


    private List<Post> preparePosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1, 1, "Title one", "Story first", false));
        posts.add(new Post(1, 2, "Title two", "Story second", false));
        posts.add(new Post(2, 3, "Three", "Story third", false));
        posts.add(new Post(2, 4, "Title four", "Story fourth", true));
        return posts;
    }

    private Post prepareFirstPost() {
        return new Post(1, 1, "Title one", "Story first", false);
    }

    private Post prepareSecondPost() {
        return new Post(1, 1, "Title twenty", "Story twenty", true);
    }


}
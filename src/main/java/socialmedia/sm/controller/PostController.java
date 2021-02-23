package socialmedia.sm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import socialmedia.sm.exceptions.PostNotFoundException;
import socialmedia.sm.model.Post;
import socialmedia.sm.service.PostService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping({"/posts", "", "/index"})
    public String showPosts(Model model) {
        List<Post> posts = postService.getPosts();
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping("/post-edit/{id}")
    public String getSinglePost(@PathVariable int id, Model model) {
        Post byId = postService.findById(id);
        model.addAttribute("singlePost", byId);
        return "post-edit";
    }

    @GetMapping("/searched")
    public String getSearchedPosts() {
        return "searched";
    }

    @PostMapping("/post-search")
    public String editDictionary(@RequestParam String title, RedirectAttributes redirectAttributes) {
        try {
            List<Post> filteredPostsBy = postService.getFilteredPostsBy(title);
            redirectAttributes.addFlashAttribute("posts", filteredPostsBy);
            return "redirect:searched";
        } catch (PostNotFoundException exception) {
            exception.getMessage();
            return "redirect:searched";
        }
    }

    @PostMapping("/edit-success")
    public String editSinglePost(@Valid @ModelAttribute Post postToEdit, Model model) {
        postService.editPost(postToEdit);
        model.addAttribute("successMessage", "edytować");
        return "success";
    }

    @PostMapping("/post-removed")
    public String deletePost(@RequestParam int id, Model model) {
        try {
            postService.deletePost(id);
            model.addAttribute("successMessage", "usunąć");
            return "success";
        } catch (PostNotFoundException exception) {
            exception.getMessage();
            return "posts";
        }
    }
}

package com.post.service.controllers;

import com.post.service.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.post.service.models.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Controller
public class WebController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public String index(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        //return "about";
        return null;
    }

    @GetMapping("/create-post")
    public String createPost(Model model) {
        return "post"; // Return the "post" template for creating a new post
    }

    @PostMapping("/create-post")
    public String createPostSubmit(@RequestParam String name, @RequestParam String title, @RequestParam String content, Model model) {
        LocalDateTime currentDateTime = LocalDateTime.now(); // Get current timestamp

        Post post = new Post();
        post.setName(name);
        post.setTitle(title);
        post.setContent(content);
        post.setCreatedAt(currentDateTime); // Set the creation timestamp
        postRepository.save(post);

        model.addAttribute("message", "Post created successfully!");
        return "redirect:/";
    }

    @GetMapping("/edit-post/{postId}")
    public String editPostForm(@PathVariable Long postId, Model model) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            model.addAttribute("post", post);
            return "edit";
        } else {
            // Handle post not found
            return "redirect:/"; // Redirect back to the index page or show an error page
        }
    }

    @PostMapping("/edit-post/{postId}")
    public String editPostSubmit(@PathVariable Long postId, @RequestParam String name, @RequestParam String title, @RequestParam String content, Model model) {
        Optional<Post> postOptional = postRepository.findById(postId);

        // Redirect back to the index page or show an error page
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setName(name);
            post.setTitle(title);
            post.setContent(content);
            postRepository.save(post);

            model.addAttribute("message", "Post updated successfully!");
        } else {
            // Handle post not found
        }
        return "redirect:/";
    }

    @GetMapping("/delete-post/{postId}")
    public String deletePost(@PathVariable Long postId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Post> postOptional = postRepository.findById(postId);

        // Redirect back to the index page or show an error page
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            postRepository.delete(post);

            model.addAttribute("message", "Post deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Delete failed. Post not found.");
        }
        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchPostsForm(Model model) {
        model.addAttribute("searchQuery", ""); // Initialize the searchQuery attribute
        return "search";
    }

    @PostMapping("/search")
    public String searchPosts(@ModelAttribute("searchQuery") String query, Model model) {
        List<Post> searchResults = postRepository.findByNameContainingIgnoreCaseOrTitleContainingIgnoreCase(query, query);
        model.addAttribute("searchResults", searchResults);
        return "search";
    }
}


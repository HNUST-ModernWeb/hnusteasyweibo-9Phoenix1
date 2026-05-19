package com.weibo.easyyweibo.controller;

import com.weibo.easyyweibo.entity.Post;
import com.weibo.easyyweibo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/public")
    public ResponseEntity<List<Post>> getPublicPosts() {
        List<Post> posts = postService.getPublicPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/visibility/{visibility}")
    public ResponseEntity<List<Post>> getPostsByVisibility(@PathVariable Integer visibility) {
        List<Post> posts = postService.getPostsByVisibility(visibility);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        Map<String, Object> response = new HashMap<>();
        if (createdPost != null) {
            response.put("code", 200);
            response.put("message", "发布成功");
            response.put("data", createdPost);
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "发布失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody Post post) {
        post.setId(id);
        boolean success = postService.updatePost(post);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "更新成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "更新失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @RequestParam Long userId) {
        boolean success = postService.deletePost(id, userId);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "删除失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePost(@PathVariable Long id) {
        boolean success = postService.likePost(id);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "点赞成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "点赞失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{id}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable Long id) {
        boolean success = postService.unlikePost(id);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "取消点赞成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "取消点赞失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam String keyword) {
        List<Post> posts = postService.searchPosts(keyword);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<?> getPostCountByUser(@PathVariable Long userId) {
        int count = postService.getPostCountByUser(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/total-likes")
    public ResponseEntity<?> getUserTotalLikes(@PathVariable Long userId) {
        int totalLikes = postService.getUserTotalLikes(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("totalLikes", totalLikes);
        return ResponseEntity.ok(response);
    }
}

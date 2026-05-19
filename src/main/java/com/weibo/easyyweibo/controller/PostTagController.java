package com.weibo.easyyweibo.controller;

import com.weibo.easyyweibo.entity.PostTag;
import com.weibo.easyyweibo.service.PostTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post-tags")
public class PostTagController {

    @Autowired
    private PostTagService postTagService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostTagById(@PathVariable Long id) {
        PostTag postTag = postTagService.getPostTagById(id);
        if (postTag != null) {
            return ResponseEntity.ok(postTag);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<PostTag>> getPostTagsByPostId(@PathVariable Long postId) {
        List<PostTag> postTags = postTagService.getPostTagsByPostId(postId);
        return ResponseEntity.ok(postTags);
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<PostTag>> getPostTagsByTagId(@PathVariable Long tagId) {
        List<PostTag> postTags = postTagService.getPostTagsByTagId(tagId);
        return ResponseEntity.ok(postTags);
    }

    @GetMapping
    public ResponseEntity<List<PostTag>> getAllPostTags() {
        List<PostTag> postTags = postTagService.getAllPostTags();
        return ResponseEntity.ok(postTags);
    }

    @PostMapping
    public ResponseEntity<?> addTagToPost(@RequestBody Map<String, Long> params) {
        Long postId = params.get("postId");
        Long tagId = params.get("tagId");
        boolean success = postTagService.addTagToPost(postId, tagId);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "标签添加成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "标签已存在或添加失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> removeTagFromPost(@RequestBody Map<String, Long> params) {
        Long postId = params.get("postId");
        Long tagId = params.get("tagId");
        boolean success = postTagService.removeTagFromPost(postId, tagId);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "标签移除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "标签移除失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> removeAllTagsFromPost(@PathVariable Long postId) {
        boolean success = postTagService.removeAllTagsFromPost(postId);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "所有标签移除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "移除失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkTagExistsInPost(@RequestParam Long postId, @RequestParam Long tagId) {
        boolean exists = postTagService.checkTagExistsInPost(postId, tagId);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<?> getTagCountByPost(@PathVariable Long postId) {
        int count = postTagService.getTagCountByPost(postId);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tag/{tagId}/count")
    public ResponseEntity<?> getPostCountByTag(@PathVariable Long tagId) {
        int count = postTagService.getPostCountByTag(tagId);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}

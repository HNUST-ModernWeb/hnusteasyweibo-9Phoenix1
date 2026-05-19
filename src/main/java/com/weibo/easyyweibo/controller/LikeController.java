package com.weibo.easyyweibo.controller;

import com.weibo.easyyweibo.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping
    public ResponseEntity<?> likePost(@RequestBody Map<String, Long> params) {
        Long userId = params.get("userId");
        Long postId = params.get("postId");
        boolean success = likeService.likePost(userId, postId);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "点赞成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "已点赞或点赞失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> unlikePost(@RequestBody Map<String, Long> params) {
        Long userId = params.get("userId");
        Long postId = params.get("postId");
        boolean success = likeService.unlikePost(userId, postId);
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

    @DeleteMapping("/unlike")
    public ResponseEntity<?> unlikePostByParams(@RequestParam Long userId, @RequestParam Long postId) {
        boolean success = likeService.unlikePost(userId, postId);
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

    @GetMapping("/check")
    public ResponseEntity<?> checkUserLiked(@RequestParam Long userId, @RequestParam Long postId) {
        boolean liked = likeService.checkUserLiked(userId, postId);
        Map<String, Object> response = new HashMap<>();
        response.put("liked", liked);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<?> getLikeCountByPost(@PathVariable Long postId) {
        int count = likeService.getLikeCountByPost(postId);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<?> getUserLikedPostIds(@PathVariable Long userId) {
        Set<Long> postIds = likeService.getUserLikedPostIds(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("postIds", postIds);
        return ResponseEntity.ok(response);
    }
}

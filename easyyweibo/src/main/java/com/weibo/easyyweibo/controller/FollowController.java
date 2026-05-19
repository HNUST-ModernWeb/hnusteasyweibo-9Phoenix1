package com.weibo.easyyweibo.controller;

import com.weibo.easyyweibo.entity.Follow;
import com.weibo.easyyweibo.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getFollowById(@PathVariable Long id) {
        Follow follow = followService.getFollowById(id);
        if (follow != null) {
            return ResponseEntity.ok(follow);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Follow>> getAllFollows() {
        List<Follow> follows = followService.getAllFollows();
        return ResponseEntity.ok(follows);
    }

    @GetMapping("/follower/{followerId}")
    public ResponseEntity<List<Follow>> getFollowsByFollowerId(@PathVariable Long followerId) {
        List<Follow> follows = followService.getFollowsByFollowerId(followerId);
        return ResponseEntity.ok(follows);
    }

    @GetMapping("/following/{followingId}")
    public ResponseEntity<List<Follow>> getFollowsByFollowingId(@PathVariable Long followingId) {
        List<Follow> follows = followService.getFollowsByFollowingId(followingId);
        return ResponseEntity.ok(follows);
    }

    @PostMapping
    public ResponseEntity<?> followUser(@RequestBody Map<String, Long> params) {
        Long followerId = params.get("followerId");
        Long followingId = params.get("followingId");
        boolean success = followService.followUser(followerId, followingId);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "关注成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "已关注或关注失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> unfollowUser(@RequestBody Map<String, Long> params) {
        Long followerId = params.get("followerId");
        Long followingId = params.get("followingId");
        boolean success = followService.unfollowUser(followerId, followingId);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "取消关注成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "取消关注失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkIsFollowing(@RequestParam Long followerId, @RequestParam Long followingId) {
        boolean isFollowing = followService.checkIsFollowing(followerId, followingId);
        Map<String, Object> response = new HashMap<>();
        response.put("isFollowing", isFollowing);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/follower/{followerId}/count")
    public ResponseEntity<?> getFollowingCount(@PathVariable Long followerId) {
        int count = followService.getFollowingCount(followerId);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/following/{followingId}/count")
    public ResponseEntity<?> getFollowersCount(@PathVariable Long followingId) {
        int count = followService.getFollowersCount(followingId);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}

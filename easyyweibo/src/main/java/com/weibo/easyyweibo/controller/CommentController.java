package com.weibo.easyyweibo.controller;

import com.weibo.easyyweibo.dto.CommentDTO;
import com.weibo.easyyweibo.entity.Comment;
import com.weibo.easyyweibo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/post/{postId}/top")
    public ResponseEntity<List<CommentDTO>> getTopLevelComments(@PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getTopLevelComments(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable Long userId) {
        List<Comment> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Comment>> getRepliesByParentId(@PathVariable Long parentId) {
        List<Comment> comments = commentService.getRepliesByParentId(parentId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {
        boolean success = commentService.createComment(comment);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "评论成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "评论失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        comment.setId(id);
        boolean success = commentService.updateComment(comment);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "评论更新成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "评论更新失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        boolean success = commentService.deleteComment(id);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "评论删除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "评论删除失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeComment(@PathVariable Long id) {
        boolean success = commentService.likeComment(id);
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
    public ResponseEntity<?> unlikeComment(@PathVariable Long id) {
        boolean success = commentService.unlikeComment(id);
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

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<?> getCommentCountByPost(@PathVariable Long postId) {
        int count = commentService.getCommentCountByPost(postId);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<?> getCommentCountByUser(@PathVariable Long userId) {
        int count = commentService.getCommentCountByUser(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}

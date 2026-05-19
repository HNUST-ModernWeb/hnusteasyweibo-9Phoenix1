package com.weibo.easyyweibo.controller;

import com.weibo.easyyweibo.entity.PostImage;
import com.weibo.easyyweibo.service.PostImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post-images")
public class PostImageController {

    @Autowired
    private PostImageService postImageService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        PostImage image = postImageService.getImageById(id);
        if (image != null) {
            return ResponseEntity.ok(image);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<PostImage>> getImagesByPostId(@PathVariable Long postId) {
        List<PostImage> images = postImageService.getImagesByPostId(postId);
        return ResponseEntity.ok(images);
    }

    @GetMapping
    public ResponseEntity<List<PostImage>> getAllImages() {
        List<PostImage> images = postImageService.getAllImages();
        return ResponseEntity.ok(images);
    }

    @PostMapping
    public ResponseEntity<?> addImage(@RequestBody PostImage postImage) {
        System.out.println("接收到的图片数据 - postId: " + postImage.getPostId());
        System.out.println("接收到的图片数据 - imageUrl长度: " + (postImage.getImageUrl() != null ? postImage.getImageUrl().length() : 0));
        System.out.println("接收到的图片数据 - sortOrder: " + postImage.getSortOrder());
        
        boolean success = postImageService.addImage(postImage);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "图片添加成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "图片添加失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<?> addImages(@RequestBody Map<String, Object> params) {
        Long postId = Long.valueOf(params.get("postId").toString());
        @SuppressWarnings("unchecked")
        List<String> imageUrls = (List<String>) params.get("imageUrls");

        boolean success = postImageService.addImages(postId, imageUrls);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "批量添加图片成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "批量添加图片失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateImage(@PathVariable Long id, @RequestBody PostImage postImage) {
        postImage.setId(id);
        boolean success = postImageService.updateImage(postImage);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "图片更新成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "图片更新失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/sort")
    public ResponseEntity<?> updateSortOrder(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Integer sortOrder = params.get("sortOrder");
        boolean success = postImageService.updateSortOrder(id, sortOrder);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "排序更新成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "排序更新失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable Long id) {
        boolean success = postImageService.deleteImage(id);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "图片删除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "图片删除失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> deleteImagesByPostId(@PathVariable Long postId) {
        boolean success = postImageService.deleteImagesByPostId(postId);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "动态图片全部删除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "删除失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<?> getImageCountByPost(@PathVariable Long postId) {
        int count = postImageService.getImageCountByPost(postId);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{postId}/first")
    public ResponseEntity<?> getFirstImageByPost(@PathVariable Long postId) {
        PostImage image = postImageService.getFirstImageByPost(postId);
        if (image != null) {
            return ResponseEntity.ok(image);
        }
        return ResponseEntity.notFound().build();
    }
}

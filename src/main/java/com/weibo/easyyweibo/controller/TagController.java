package com.weibo.easyyweibo.controller;

import com.weibo.easyyweibo.entity.Tag;
import com.weibo.easyyweibo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTagById(@PathVariable Long id) {
        Tag tag = tagService.getTagById(id);
        if (tag != null) {
            return ResponseEntity.ok(tag);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getTagByName(@PathVariable String name) {
        Tag tag = tagService.getTagByName(name);
        if (tag != null) {
            return ResponseEntity.ok(tag);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Tag>> getPopularTags(@RequestParam(defaultValue = "10") int limit) {
        List<Tag> tags = tagService.getPopularTags(limit);
        return ResponseEntity.ok(tags);
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        boolean success = tagService.createTag(tag);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "标签创建成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "标签已存在");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        tag.setId(id);
        boolean success = tagService.updateTag(tag);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "标签更新成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "标签更新失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        boolean success = tagService.deleteTag(id);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "标签删除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "标签删除失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkTagExists(@RequestParam String name) {
        boolean exists = tagService.checkTagExists(name);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}

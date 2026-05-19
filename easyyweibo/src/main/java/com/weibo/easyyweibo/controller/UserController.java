package com.weibo.easyyweibo.controller;

import com.weibo.easyyweibo.entity.User;
import com.weibo.easyyweibo.service.UserService;
import com.weibo.easyyweibo.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            user.setPassword(null);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<?> getUserByPhone(@PathVariable String phone) {
        User user = userService.getUserByPhone(phone);
        if (user != null) {
            user.setPassword(null);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        users.forEach(user -> user.setPassword(null));
        return ResponseEntity.ok(users);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<User>> getUsersByStatus(@PathVariable Integer status) {
        List<User> users = userService.getUsersByStatus(status);
        users.forEach(user -> user.setPassword(null));
        return ResponseEntity.ok(users);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        boolean success = userService.register(user);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "注册成功");
            response.put("data", user);
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "手机号或邮箱已存在");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String phone = credentials.get("phone");
        String password = credentials.get("password");
        
        User user = userService.getUserByPhone(phone);
        Map<String, Object> response = new HashMap<>();
        
        if (user != null && PasswordUtil.matches(password, user.getPassword())) {
            user.setPassword(null);
            response.put("code", 200);
            response.put("message", "登录成功");
            response.put("data", user);
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 401);
            response.put("message", "手机号或密码错误");
            return ResponseEntity.status(401).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        boolean success = userService.updateUser(user);
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

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String password = params.get("password");
        boolean success = userService.updatePassword(id, password);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "密码修改成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "密码修改失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean success = userService.deleteUser(id);
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

    @GetMapping("/check-phone")
    public ResponseEntity<?> checkPhone(@RequestParam String phone) {
        boolean exists = userService.checkPhoneExists(phone);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = userService.checkEmailExists(email);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendedUsers(@RequestParam Long userId, @RequestParam(defaultValue = "4") int limit) {
        List<User> users = userService.getUnfollowedUsers(userId, limit);
        users.forEach(user -> user.setPassword(null));
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{userId}/follow/{targetUserId}")
    public ResponseEntity<?> followUser(@PathVariable Long userId, @PathVariable Long targetUserId) {
        boolean success = userService.followUser(userId, targetUserId);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "关注成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "关注失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{userId}/unfollow/{targetUserId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long userId, @PathVariable Long targetUserId) {
        boolean success = userService.unfollowUser(userId, targetUserId);
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
}

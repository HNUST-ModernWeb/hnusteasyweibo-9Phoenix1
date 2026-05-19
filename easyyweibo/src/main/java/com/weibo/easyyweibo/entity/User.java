package com.weibo.easyyweibo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String phone;           // 手机号（登录账号）
    private String password;
    private String nickname;
    private String avatar;
    private String bio;
    private String location;    // 位置
    private String email;
    private Integer status;
    private Integer followersCount;
    private Integer followingCount;
    private Integer postsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
}

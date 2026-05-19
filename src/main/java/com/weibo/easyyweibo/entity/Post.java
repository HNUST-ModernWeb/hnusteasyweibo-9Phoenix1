package com.weibo.easyyweibo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Post {
    private Long id;
    private Long userId;
    private String content;
    private Integer visibility;
    private Integer likesCount;
    private Integer commentsCount;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

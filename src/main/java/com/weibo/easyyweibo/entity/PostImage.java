package com.weibo.easyyweibo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostImage {
    private Long id;
    private Long postId;
    private String imageUrl;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}

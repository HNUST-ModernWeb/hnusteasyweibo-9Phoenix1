package com.weibo.easyyweibo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Notification {
    private Long id;
    private Long userId;
    private Long senderId;
    private Integer type;
    private Long postId;
    private Long commentId;
    private String content;
    private Integer isRead;
    private LocalDateTime createdAt;
}

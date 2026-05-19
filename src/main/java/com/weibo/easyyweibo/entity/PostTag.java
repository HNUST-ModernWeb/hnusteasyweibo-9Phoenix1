package com.weibo.easyyweibo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostTag {
    private Long id;
    private Long postId;
    private Long tagId;
    private LocalDateTime createdAt;
}

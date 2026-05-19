package com.weibo.easyyweibo.service;

import com.weibo.easyyweibo.dao.PostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LikeService {

    @Autowired
    private PostDao postDao;

    // 使用内存存储用户点赞状态（替代 likes 表）
    // key: postId_userId, value: true
    private static final Map<String, Boolean> userLikes = new ConcurrentHashMap<>();

    private String getKey(Long userId, Long postId) {
        return postId + "_" + userId;
    }

    @Transactional
    public boolean likePost(Long userId, Long postId) {
        String key = getKey(userId, postId);
        if (userLikes.containsKey(key)) {
            return false; // 已经点赞过了
        }
        userLikes.put(key, true);
        postDao.incrementLikesCount(postId);
        return true;
    }

    @Transactional
    public boolean unlikePost(Long userId, Long postId) {
        String key = getKey(userId, postId);
        if (!userLikes.containsKey(key)) {
            return false; // 没有点赞过
        }
        userLikes.remove(key);
        postDao.decrementLikesCount(postId);
        return true;
    }

    public boolean checkUserLiked(Long userId, Long postId) {
        return userLikes.containsKey(getKey(userId, postId));
    }

    public int getLikeCountByPost(Long postId) {
        // 从 posts 表获取点赞数
        return postDao.findLikesCountById(postId);
    }

    public Set<Long> getUserLikedPostIds(Long userId) {
        Set<Long> postIds = new HashSet<>();
        for (String key : userLikes.keySet()) {
            if (key.endsWith("_" + userId)) {
                String postIdStr = key.substring(0, key.lastIndexOf("_"));
                postIds.add(Long.parseLong(postIdStr));
            }
        }
        return postIds;
    }

    @Transactional
    public boolean deleteLikesByPostId(Long postId) {
        // 删除该动态的所有点赞记录
        userLikes.entrySet().removeIf(entry -> entry.getKey().startsWith(postId + "_"));
        return true;
    }
}

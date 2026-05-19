package com.weibo.easyyweibo.service;

import com.weibo.easyyweibo.dao.CommentDao;
import com.weibo.easyyweibo.dao.PostDao;
import com.weibo.easyyweibo.dao.UserDao;
import com.weibo.easyyweibo.dto.CommentDTO;
import com.weibo.easyyweibo.entity.Comment;
import com.weibo.easyyweibo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private UserDao userDao;

    public Comment getCommentById(Long id) {
        return commentDao.findById(id);
    }

    public List<Comment> getAllComments() {
        return commentDao.findAll();
    }

    public List<CommentDTO> getCommentsByPostId(Long postId) {
        // 获取所有评论
        List<Comment> allComments = commentDao.findByPostId(postId);
        
        // 分离一级评论和回复
        List<Comment> topLevelComments = allComments.stream()
            .filter(c -> c.getParentId() == null)
            .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt())) // 一级评论按时间倒序
            .collect(Collectors.toList());
        
        List<CommentDTO> result = new ArrayList<>();
        
        for (Comment comment : topLevelComments) {
            CommentDTO dto = convertToDTO(comment);
            result.add(dto);
            
            // 查找该评论的回复，按时间正序（最早的在上）
            List<Comment> replies = allComments.stream()
                .filter(c -> comment.getId().equals(c.getParentId()))
                .sorted((c1, c2) -> c1.getCreatedAt().compareTo(c2.getCreatedAt())) // 回复按时间正序
                .collect(Collectors.toList());
            
            for (Comment reply : replies) {
                result.add(convertToDTO(reply));
            }
        }
        
        return result;
    }

    public List<Comment> getCommentsByUserId(Long userId) {
        return commentDao.findByUserId(userId);
    }

    public List<Comment> getRepliesByParentId(Long parentId) {
        return commentDao.findByParentId(parentId);
    }

    public List<CommentDTO> getTopLevelComments(Long postId) {
        List<Comment> comments = commentDao.findTopLevelByPostId(postId);
        return comments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public boolean createComment(Comment comment) {
        int result = commentDao.insert(comment);
        if (result > 0) {
            postDao.incrementCommentsCount(comment.getPostId());
            return true;
        }
        return false;
    }

    public boolean updateComment(Comment comment) {
        return commentDao.update(comment) > 0;
    }

    @Transactional
    public boolean deleteComment(Long id) {
        Comment comment = commentDao.findById(id);
        if (comment != null) {
            int result = commentDao.delete(id);
            if (result > 0) {
                postDao.decrementCommentsCount(comment.getPostId());
                return true;
            }
        }
        return false;
    }

    public boolean likeComment(Long id) {
        return commentDao.incrementLikesCount(id) > 0;
    }

    public boolean unlikeComment(Long id) {
        return commentDao.decrementLikesCount(id) > 0;
    }

    public int getCommentCountByPost(Long postId) {
        return commentDao.countByPostId(postId);
    }

    public int getCommentCountByUser(Long userId) {
        return commentDao.countByUserId(userId);
    }

    // 转换为 DTO，包含用户信息
    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPostId());
        dto.setUserId(comment.getUserId());
        dto.setParentId(comment.getParentId());
        dto.setContent(comment.getContent());
        dto.setLikesCount(comment.getLikesCount());
        dto.setStatus(comment.getStatus());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());

        // 查询用户信息
        User user = userDao.findById(comment.getUserId());
        if (user != null) {
            dto.setUserNickname(user.getNickname());
            dto.setUserAvatar(user.getAvatar());
        }

        return dto;
    }
}

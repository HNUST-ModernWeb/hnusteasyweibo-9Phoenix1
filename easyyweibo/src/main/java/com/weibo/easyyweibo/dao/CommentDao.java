package com.weibo.easyyweibo.dao;

import com.weibo.easyyweibo.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class CommentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Comment> commentRowMapper = new RowMapper<Comment>() {
        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Comment comment = new Comment();
            comment.setId(rs.getLong("id"));
            comment.setPostId(rs.getLong("post_id"));
            comment.setUserId(rs.getLong("user_id"));
            comment.setParentId(rs.getLong("parent_id"));
            if (rs.wasNull()) {
                comment.setParentId(null);
            }
            comment.setContent(rs.getString("content"));
            comment.setLikesCount(rs.getInt("likes_count"));
            comment.setStatus(rs.getInt("status"));

            Timestamp createdAt = rs.getTimestamp("created_at");
            comment.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

            Timestamp updatedAt = rs.getTimestamp("updated_at");
            comment.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

            return comment;
        }
    };

    public Comment findById(Long id) {
        String sql = "SELECT * FROM comments WHERE id = ? AND status = 1";
        List<Comment> comments = jdbcTemplate.query(sql, commentRowMapper, id);
        return comments.isEmpty() ? null : comments.get(0);
    }

    public List<Comment> findAll() {
        String sql = "SELECT * FROM comments WHERE status = 1 ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, commentRowMapper);
    }

    public List<Comment> findByPostId(Long postId) {
        String sql = "SELECT * FROM comments WHERE post_id = ? AND status = 1 ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, commentRowMapper, postId);
    }

    public List<Comment> findByUserId(Long userId) {
        String sql = "SELECT * FROM comments WHERE user_id = ? AND status = 1 ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, commentRowMapper, userId);
    }

    public List<Comment> findByParentId(Long parentId) {
        String sql = "SELECT * FROM comments WHERE parent_id = ? AND status = 1 ORDER BY created_at ASC";
        return jdbcTemplate.query(sql, commentRowMapper, parentId);
    }

    public List<Comment> findTopLevelByPostId(Long postId) {
        String sql = "SELECT * FROM comments WHERE post_id = ? AND parent_id IS NULL AND status = 1 ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, commentRowMapper, postId);
    }

    public int insert(Comment comment) {
        String sql = "INSERT INTO comments (post_id, user_id, parent_id, content, likes_count, status, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";
        return jdbcTemplate.update(sql,
                comment.getPostId(),
                comment.getUserId(),
                comment.getParentId(),
                comment.getContent(),
                comment.getLikesCount() != null ? comment.getLikesCount() : 0,
                comment.getStatus() != null ? comment.getStatus() : 1
        );
    }

    public int update(Comment comment) {
        String sql = "UPDATE comments SET content = ?, updated_at = NOW() WHERE id = ? AND status = 1";
        return jdbcTemplate.update(sql, comment.getContent(), comment.getId());
    }

    public int delete(Long id) {
        String sql = "UPDATE comments SET status = 0, updated_at = NOW() WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int incrementLikesCount(Long id) {
        String sql = "UPDATE comments SET likes_count = likes_count + 1 WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int decrementLikesCount(Long id) {
        String sql = "UPDATE comments SET likes_count = likes_count - 1 WHERE id = ? AND likes_count > 0";
        return jdbcTemplate.update(sql, id);
    }

    public int countByPostId(Long postId) {
        String sql = "SELECT COUNT(*) FROM comments WHERE post_id = ? AND status = 1";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, postId);
        return count != null ? count : 0;
    }

    public int countByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM comments WHERE user_id = ? AND status = 1";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }
}

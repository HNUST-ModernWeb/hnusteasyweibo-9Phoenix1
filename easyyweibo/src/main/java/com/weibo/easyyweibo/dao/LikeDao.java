package com.weibo.easyyweibo.dao;

import com.weibo.easyyweibo.entity.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class LikeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Like> likeRowMapper = new RowMapper<Like>() {
        @Override
        public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
            Like like = new Like();
            like.setId(rs.getLong("id"));
            like.setUserId(rs.getLong("user_id"));
            like.setPostId(rs.getLong("post_id"));

            Timestamp createdAt = rs.getTimestamp("created_at");
            like.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

            return like;
        }
    };

    public Like findById(Long id) {
        String sql = "SELECT * FROM likes WHERE id = ?";
        List<Like> likes = jdbcTemplate.query(sql, likeRowMapper, id);
        return likes.isEmpty() ? null : likes.get(0);
    }

    public List<Like> findByUserId(Long userId) {
        String sql = "SELECT * FROM likes WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, likeRowMapper, userId);
    }

    public List<Like> findByPostId(Long postId) {
        String sql = "SELECT * FROM likes WHERE post_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, likeRowMapper, postId);
    }

    public List<Like> findAll() {
        String sql = "SELECT * FROM likes ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, likeRowMapper);
    }

    public int insert(Like like) {
        String sql = "INSERT INTO likes (user_id, post_id, created_at) VALUES (?, ?, NOW())";
        return jdbcTemplate.update(sql, like.getUserId(), like.getPostId());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM likes WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int deleteByUserIdAndPostId(Long userId, Long postId) {
        String sql = "DELETE FROM likes WHERE user_id = ? AND post_id = ?";
        return jdbcTemplate.update(sql, userId, postId);
    }

    public int deleteByUserId(Long userId) {
        String sql = "DELETE FROM likes WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }

    public int deleteByPostId(Long postId) {
        String sql = "DELETE FROM likes WHERE post_id = ?";
        return jdbcTemplate.update(sql, postId);
    }

    public boolean existsByUserIdAndPostId(Long userId, Long postId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE user_id = ? AND post_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, postId);
        return count != null && count > 0;
    }

    public int countByPostId(Long postId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE post_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, postId);
        return count != null ? count : 0;
    }

    public int countByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }
}

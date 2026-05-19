package com.weibo.easyyweibo.dao;

import com.weibo.easyyweibo.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class PostDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Post> postRowMapper = new RowMapper<Post>() {
        @Override
        public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            Post post = new Post();
            post.setId(rs.getLong("id"));
            post.setUserId(rs.getLong("user_id"));
            post.setContent(rs.getString("content"));
            post.setVisibility(rs.getInt("visibility"));
            post.setLikesCount(rs.getInt("likes_count"));
            post.setCommentsCount(rs.getInt("comments_count"));
            post.setStatus(rs.getInt("status"));
            
            Timestamp createdAt = rs.getTimestamp("created_at");
            post.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
            
            Timestamp updatedAt = rs.getTimestamp("updated_at");
            post.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);
            
            return post;
        }
    };

    public Post findById(Long id) {
        String sql = "SELECT * FROM posts WHERE id = ? AND status = 1";
        List<Post> posts = jdbcTemplate.query(sql, postRowMapper, id);
        return posts.isEmpty() ? null : posts.get(0);
    }

    public List<Post> findAll() {
        String sql = "SELECT * FROM posts WHERE status = 1 ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, postRowMapper);
    }

    public List<Post> findByUserId(Long userId) {
        String sql = "SELECT * FROM posts WHERE user_id = ? AND status = 1 ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, postRowMapper, userId);
    }

    public List<Post> findByUserIdAndVisibility(Long userId, Integer visibility) {
        String sql = "SELECT * FROM posts WHERE user_id = ? AND visibility = ? AND status = 1 ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, postRowMapper, userId, visibility);
    }

    public List<Post> findByVisibility(Integer visibility) {
        String sql = "SELECT * FROM posts WHERE visibility = ? AND status = 1 ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, postRowMapper, visibility);
    }

    public List<Post> findPublicPosts() {
        String sql = "SELECT * FROM posts WHERE visibility = 1 AND status = 1 ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, postRowMapper);
    }

    public Long insert(Post post) {
        String sql = "INSERT INTO posts (user_id, content, visibility, likes_count, comments_count, status, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, post.getUserId());
            ps.setString(2, post.getContent());
            ps.setInt(3, post.getVisibility() != null ? post.getVisibility() : 1);
            ps.setInt(4, post.getLikesCount() != null ? post.getLikesCount() : 0);
            ps.setInt(5, post.getCommentsCount() != null ? post.getCommentsCount() : 0);
            ps.setInt(6, post.getStatus() != null ? post.getStatus() : 1);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public int update(Post post) {
        String sql = "UPDATE posts SET content = ?, visibility = ?, updated_at = NOW() WHERE id = ? AND status = 1";
        return jdbcTemplate.update(sql,
                post.getContent(),
                post.getVisibility(),
                post.getId()
        );
    }

    public int delete(Long id) {
        String sql = "UPDATE posts SET status = 0, updated_at = NOW() WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int incrementLikesCount(Long id) {
        String sql = "UPDATE posts SET likes_count = likes_count + 1 WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int decrementLikesCount(Long id) {
        String sql = "UPDATE posts SET likes_count = likes_count - 1 WHERE id = ? AND likes_count > 0";
        return jdbcTemplate.update(sql, id);
    }

    public int incrementCommentsCount(Long id) {
        String sql = "UPDATE posts SET comments_count = comments_count + 1 WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int decrementCommentsCount(Long id) {
        String sql = "UPDATE posts SET comments_count = comments_count - 1 WHERE id = ? AND comments_count > 0";
        return jdbcTemplate.update(sql, id);
    }

    public int findLikesCountById(Long id) {
        String sql = "SELECT likes_count FROM posts WHERE id = ? AND status = 1";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
            return count != null ? count : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public int countByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM posts WHERE user_id = ? AND status = 1";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    public List<Post> findByContentContaining(String keyword) {
        String sql = "SELECT * FROM posts WHERE content LIKE ? AND status = 1 ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, postRowMapper, "%" + keyword + "%");
    }

    public int sumLikesCountByUserId(Long userId) {
        String sql = "SELECT SUM(likes_count) FROM posts WHERE user_id = ? AND status = 1";
        try {
            Integer sum = jdbcTemplate.queryForObject(sql, Integer.class, userId);
            return sum != null ? sum : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}

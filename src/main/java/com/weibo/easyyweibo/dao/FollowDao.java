package com.weibo.easyyweibo.dao;

import com.weibo.easyyweibo.entity.Follow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class FollowDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Follow> followRowMapper = new RowMapper<Follow>() {
        @Override
        public Follow mapRow(ResultSet rs, int rowNum) throws SQLException {
            Follow follow = new Follow();
            follow.setId(rs.getLong("id"));
            follow.setFollowerId(rs.getLong("follower_id"));
            follow.setFollowingId(rs.getLong("following_id"));

            Timestamp createdAt = rs.getTimestamp("created_at");
            follow.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

            return follow;
        }
    };

    public Follow findById(Long id) {
        String sql = "SELECT * FROM follows WHERE id = ?";
        List<Follow> follows = jdbcTemplate.query(sql, followRowMapper, id);
        return follows.isEmpty() ? null : follows.get(0);
    }

    public List<Follow> findAll() {
        String sql = "SELECT * FROM follows ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, followRowMapper);
    }

    public List<Follow> findByFollowerId(Long followerId) {
        String sql = "SELECT * FROM follows WHERE follower_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, followRowMapper, followerId);
    }

    public List<Follow> findByFollowingId(Long followingId) {
        String sql = "SELECT * FROM follows WHERE following_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, followRowMapper, followingId);
    }

    public int insert(Follow follow) {
        String sql = "INSERT INTO follows (follower_id, following_id, created_at) VALUES (?, ?, NOW())";
        return jdbcTemplate.update(sql, follow.getFollowerId(), follow.getFollowingId());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM follows WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int deleteByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        String sql = "DELETE FROM follows WHERE follower_id = ? AND following_id = ?";
        return jdbcTemplate.update(sql, followerId, followingId);
    }

    public boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        String sql = "SELECT COUNT(*) FROM follows WHERE follower_id = ? AND following_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, followerId, followingId);
        return count != null && count > 0;
    }

    public int countByFollowerId(Long followerId) {
        String sql = "SELECT COUNT(*) FROM follows WHERE follower_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, followerId);
        return count != null ? count : 0;
    }

    public int countByFollowingId(Long followingId) {
        String sql = "SELECT COUNT(*) FROM follows WHERE following_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, followingId);
        return count != null ? count : 0;
    }
}

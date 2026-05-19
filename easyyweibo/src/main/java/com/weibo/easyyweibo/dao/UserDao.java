package com.weibo.easyyweibo.dao;

import com.weibo.easyyweibo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setPhone(rs.getString("phone"));
            user.setPassword(rs.getString("password"));
            user.setNickname(rs.getString("nickname"));
            user.setAvatar(rs.getString("avatar"));
            user.setBio(rs.getString("bio"));
            user.setEmail(rs.getString("email"));
            user.setStatus(rs.getInt("status"));
            user.setFollowersCount(rs.getInt("followers_count"));
            user.setFollowingCount(rs.getInt("following_count"));
            user.setPostsCount(rs.getInt("posts_count"));
            
            Timestamp createdAt = rs.getTimestamp("created_at");
            user.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
            
            Timestamp updatedAt = rs.getTimestamp("updated_at");
            user.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);
            
            Timestamp lastLoginAt = rs.getTimestamp("last_login_at");
            user.setLastLoginAt(lastLoginAt != null ? lastLoginAt.toLocalDateTime() : null);
            
            return user;
        }
    };

    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, id);
        return users.isEmpty() ? null : users.get(0);
    }

    public User findByPhone(String phone) {
        String sql = "SELECT * FROM users WHERE phone = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, phone);
        return users.isEmpty() ? null : users.get(0);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public List<User> findByStatus(Integer status) {
        String sql = "SELECT * FROM users WHERE status = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, userRowMapper, status);
    }

    public int insert(User user) {
        String sql = "INSERT INTO users (phone, password, nickname, avatar, bio, email, status, " +
                     "followers_count, following_count, posts_count, created_at, updated_at, last_login_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), ?)";
        return jdbcTemplate.update(sql,
                user.getPhone(),
                user.getPassword(),
                user.getNickname(),
                user.getAvatar(),
                user.getBio(),
                user.getEmail(),
                user.getStatus() != null ? user.getStatus() : 1,
                user.getFollowersCount() != null ? user.getFollowersCount() : 0,
                user.getFollowingCount() != null ? user.getFollowingCount() : 0,
                user.getPostsCount() != null ? user.getPostsCount() : 0,
                user.getLastLoginAt() != null ? Timestamp.valueOf(user.getLastLoginAt()) : null
        );
    }

    public int update(User user) {
        String sql = "UPDATE users SET nickname = ?, avatar = ?, bio = ?, location = ?, email = ?, " +
                     "status = ?, updated_at = NOW() WHERE id = ?";
        return jdbcTemplate.update(sql,
                user.getNickname(),
                user.getAvatar(),
                user.getBio(),
                user.getLocation(),
                user.getEmail(),
                user.getStatus() != null ? user.getStatus() : 1,
                user.getId()
        );
    }

    public int updatePassword(Long id, String password) {
        String sql = "UPDATE users SET password = ?, updated_at = NOW() WHERE id = ?";
        return jdbcTemplate.update(sql, password, id);
    }

    public int updateLastLogin(Long id) {
        String sql = "UPDATE users SET last_login_at = NOW() WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int incrementFollowersCount(Long id) {
        String sql = "UPDATE users SET followers_count = followers_count + 1 WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int decrementFollowersCount(Long id) {
        String sql = "UPDATE users SET followers_count = followers_count - 1 WHERE id = ? AND followers_count > 0";
        return jdbcTemplate.update(sql, id);
    }

    public int incrementFollowingCount(Long id) {
        String sql = "UPDATE users SET following_count = following_count + 1 WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int decrementFollowingCount(Long id) {
        String sql = "UPDATE users SET following_count = following_count - 1 WHERE id = ? AND following_count > 0";
        return jdbcTemplate.update(sql, id);
    }

    public int incrementPostsCount(Long id) {
        String sql = "UPDATE users SET posts_count = posts_count + 1 WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int decrementPostsCount(Long id) {
        String sql = "UPDATE users SET posts_count = posts_count - 1 WHERE id = ? AND posts_count > 0";
        return jdbcTemplate.update(sql, id);
    }

    public int delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public boolean existsByPhone(String phone) {
        String sql = "SELECT COUNT(*) FROM users WHERE phone = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phone);
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public List<User> findUnfollowedUsers(Long userId, int limit) {
        // 首先获取未关注的用户
        String sql = "SELECT * FROM users WHERE id != ? AND id NOT IN " +
                     "(SELECT following_id FROM follows WHERE follower_id = ?) " +
                     "ORDER BY followers_count DESC LIMIT ?";
        List<User> unfollowedUsers = jdbcTemplate.query(sql, userRowMapper, userId, userId, limit);
        
        // 如果未关注用户不足，补充一些已关注的用户（按粉丝数排序）
        if (unfollowedUsers.size() < limit) {
            int remaining = limit - unfollowedUsers.size();
            String existingUserIds = unfollowedUsers.stream()
                .map(u -> u.getId().toString())
                .reduce((a, b) -> a + "," + b)
                .orElse("");
            
            String additionalSql;
            if (existingUserIds.isEmpty()) {
                additionalSql = "SELECT * FROM users WHERE id != ? ORDER BY followers_count DESC LIMIT ?";
            } else {
                additionalSql = "SELECT * FROM users WHERE id != ? AND id NOT IN (" + existingUserIds + ") " +
                               "ORDER BY followers_count DESC LIMIT ?";
            }
            List<User> additionalUsers = jdbcTemplate.query(additionalSql, userRowMapper, userId, remaining);
            unfollowedUsers.addAll(additionalUsers);
        }
        
        return unfollowedUsers;
    }
}

package com.weibo.easyyweibo.dao;

import com.weibo.easyyweibo.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class NotificationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Notification> notificationRowMapper = new RowMapper<Notification>() {
        @Override
        public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
            Notification notification = new Notification();
            notification.setId(rs.getLong("id"));
            notification.setUserId(rs.getLong("user_id"));
            notification.setSenderId(rs.getLong("sender_id"));
            if (rs.wasNull()) {
                notification.setSenderId(null);
            }
            notification.setType(rs.getInt("type"));
            notification.setPostId(rs.getLong("post_id"));
            if (rs.wasNull()) {
                notification.setPostId(null);
            }
            notification.setCommentId(rs.getLong("comment_id"));
            if (rs.wasNull()) {
                notification.setCommentId(null);
            }
            notification.setContent(rs.getString("content"));
            notification.setIsRead(rs.getInt("is_read"));

            Timestamp createdAt = rs.getTimestamp("created_at");
            notification.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

            return notification;
        }
    };

    public Notification findById(Long id) {
        String sql = "SELECT * FROM notifications WHERE id = ?";
        List<Notification> notifications = jdbcTemplate.query(sql, notificationRowMapper, id);
        return notifications.isEmpty() ? null : notifications.get(0);
    }

    public List<Notification> findAll() {
        String sql = "SELECT * FROM notifications ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, notificationRowMapper);
    }

    public List<Notification> findByUserId(Long userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, notificationRowMapper, userId);
    }

    public List<Notification> findUnreadByUserId(Long userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = 0 ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, notificationRowMapper, userId);
    }

    public List<Notification> findByUserIdAndType(Long userId, Integer type) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND type = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, notificationRowMapper, userId, type);
    }

    public int insert(Notification notification) {
        String sql = "INSERT INTO notifications (user_id, sender_id, type, post_id, comment_id, content, is_read, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        return jdbcTemplate.update(sql,
                notification.getUserId(),
                notification.getSenderId(),
                notification.getType(),
                notification.getPostId(),
                notification.getCommentId(),
                notification.getContent(),
                notification.getIsRead() != null ? notification.getIsRead() : 0
        );
    }

    public int update(Notification notification) {
        String sql = "UPDATE notifications SET is_read = ? WHERE id = ?";
        return jdbcTemplate.update(sql, notification.getIsRead(), notification.getId());
    }

    public int markAsRead(Long id) {
        String sql = "UPDATE notifications SET is_read = 1 WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int markAllAsReadByUserId(Long userId) {
        String sql = "UPDATE notifications SET is_read = 1 WHERE user_id = ? AND is_read = 0";
        return jdbcTemplate.update(sql, userId);
    }

    public int delete(Long id) {
        String sql = "DELETE FROM notifications WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int deleteByUserId(Long userId) {
        String sql = "DELETE FROM notifications WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }

    public int countUnreadByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = 0";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    public int countByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }
}

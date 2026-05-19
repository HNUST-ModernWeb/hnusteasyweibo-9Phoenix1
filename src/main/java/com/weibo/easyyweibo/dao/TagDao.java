package com.weibo.easyyweibo.dao;

import com.weibo.easyyweibo.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class TagDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Tag> tagRowMapper = new RowMapper<Tag>() {
        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tag tag = new Tag();
            tag.setId(rs.getLong("id"));
            tag.setName(rs.getString("name"));
            tag.setUsageCount(rs.getInt("usage_count"));
            
            Timestamp createdAt = rs.getTimestamp("created_at");
            tag.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
            
            return tag;
        }
    };

    public Tag findById(Long id) {
        String sql = "SELECT * FROM tags WHERE id = ?";
        List<Tag> tags = jdbcTemplate.query(sql, tagRowMapper, id);
        return tags.isEmpty() ? null : tags.get(0);
    }

    public Tag findByName(String name) {
        String sql = "SELECT * FROM tags WHERE name = ?";
        List<Tag> tags = jdbcTemplate.query(sql, tagRowMapper, name);
        return tags.isEmpty() ? null : tags.get(0);
    }

    public List<Tag> findAll() {
        String sql = "SELECT * FROM tags ORDER BY usage_count DESC";
        return jdbcTemplate.query(sql, tagRowMapper);
    }

    public List<Tag> findPopularTags(int limit) {
        String sql = "SELECT * FROM tags ORDER BY usage_count DESC LIMIT ?";
        return jdbcTemplate.query(sql, tagRowMapper, limit);
    }

    public int insert(Tag tag) {
        String sql = "INSERT INTO tags (name, usage_count, created_at) VALUES (?, ?, NOW())";
        return jdbcTemplate.update(sql, tag.getName(), tag.getUsageCount() != null ? tag.getUsageCount() : 0);
    }

    public int update(Tag tag) {
        String sql = "UPDATE tags SET name = ? WHERE id = ?";
        return jdbcTemplate.update(sql, tag.getName(), tag.getId());
    }

    public int incrementUsageCount(Long id) {
        String sql = "UPDATE tags SET usage_count = usage_count + 1 WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int decrementUsageCount(Long id) {
        String sql = "UPDATE tags SET usage_count = usage_count - 1 WHERE id = ? AND usage_count > 0";
        return jdbcTemplate.update(sql, id);
    }

    public int delete(Long id) {
        String sql = "DELETE FROM tags WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM tags WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }
}

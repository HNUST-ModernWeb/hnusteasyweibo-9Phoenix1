package com.weibo.easyyweibo.dao;

import com.weibo.easyyweibo.entity.PostTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class PostTagDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<PostTag> postTagRowMapper = new RowMapper<PostTag>() {
        @Override
        public PostTag mapRow(ResultSet rs, int rowNum) throws SQLException {
            PostTag postTag = new PostTag();
            postTag.setId(rs.getLong("id"));
            postTag.setPostId(rs.getLong("post_id"));
            postTag.setTagId(rs.getLong("tag_id"));

            Timestamp createdAt = rs.getTimestamp("created_at");
            postTag.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

            return postTag;
        }
    };

    public PostTag findById(Long id) {
        String sql = "SELECT * FROM post_tags WHERE id = ?";
        List<PostTag> postTags = jdbcTemplate.query(sql, postTagRowMapper, id);
        return postTags.isEmpty() ? null : postTags.get(0);
    }

    public List<PostTag> findByPostId(Long postId) {
        String sql = "SELECT * FROM post_tags WHERE post_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, postTagRowMapper, postId);
    }

    public List<PostTag> findByTagId(Long tagId) {
        String sql = "SELECT * FROM post_tags WHERE tag_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, postTagRowMapper, tagId);
    }

    public List<PostTag> findAll() {
        String sql = "SELECT * FROM post_tags ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, postTagRowMapper);
    }

    public int insert(PostTag postTag) {
        String sql = "INSERT INTO post_tags (post_id, tag_id, created_at) VALUES (?, ?, NOW())";
        return jdbcTemplate.update(sql, postTag.getPostId(), postTag.getTagId());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM post_tags WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int deleteByPostId(Long postId) {
        String sql = "DELETE FROM post_tags WHERE post_id = ?";
        return jdbcTemplate.update(sql, postId);
    }

    public int deleteByTagId(Long tagId) {
        String sql = "DELETE FROM post_tags WHERE tag_id = ?";
        return jdbcTemplate.update(sql, tagId);
    }

    public int deleteByPostIdAndTagId(Long postId, Long tagId) {
        String sql = "DELETE FROM post_tags WHERE post_id = ? AND tag_id = ?";
        return jdbcTemplate.update(sql, postId, tagId);
    }

    public boolean existsByPostIdAndTagId(Long postId, Long tagId) {
        String sql = "SELECT COUNT(*) FROM post_tags WHERE post_id = ? AND tag_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, postId, tagId);
        return count != null && count > 0;
    }

    public int countByPostId(Long postId) {
        String sql = "SELECT COUNT(*) FROM post_tags WHERE post_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, postId);
        return count != null ? count : 0;
    }

    public int countByTagId(Long tagId) {
        String sql = "SELECT COUNT(*) FROM post_tags WHERE tag_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tagId);
        return count != null ? count : 0;
    }
}

package com.weibo.easyyweibo.dao;

import com.weibo.easyyweibo.entity.PostImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class PostImageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<PostImage> postImageRowMapper = new RowMapper<PostImage>() {
        @Override
        public PostImage mapRow(ResultSet rs, int rowNum) throws SQLException {
            PostImage postImage = new PostImage();
            postImage.setId(rs.getLong("id"));
            postImage.setPostId(rs.getLong("post_id"));
            postImage.setImageUrl(rs.getString("image_url"));
            postImage.setSortOrder(rs.getInt("sort_order"));

            Timestamp createdAt = rs.getTimestamp("created_at");
            postImage.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

            return postImage;
        }
    };

    public PostImage findById(Long id) {
        String sql = "SELECT * FROM post_images WHERE id = ?";
        List<PostImage> images = jdbcTemplate.query(sql, postImageRowMapper, id);
        return images.isEmpty() ? null : images.get(0);
    }

    public List<PostImage> findByPostId(Long postId) {
        String sql = "SELECT * FROM post_images WHERE post_id = ? ORDER BY sort_order ASC, created_at ASC";
        return jdbcTemplate.query(sql, postImageRowMapper, postId);
    }

    public List<PostImage> findAll() {
        String sql = "SELECT * FROM post_images ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, postImageRowMapper);
    }

    public int insert(PostImage postImage) {
        String sql = "INSERT INTO post_images (post_id, image_url, sort_order, created_at) VALUES (?, ?, ?, NOW())";
        return jdbcTemplate.update(sql,
                postImage.getPostId(),
                postImage.getImageUrl(),
                postImage.getSortOrder() != null ? postImage.getSortOrder() : 0
        );
    }

    public int update(PostImage postImage) {
        String sql = "UPDATE post_images SET image_url = ?, sort_order = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                postImage.getImageUrl(),
                postImage.getSortOrder(),
                postImage.getId()
        );
    }

    public int updateSortOrder(Long id, Integer sortOrder) {
        String sql = "UPDATE post_images SET sort_order = ? WHERE id = ?";
        return jdbcTemplate.update(sql, sortOrder, id);
    }

    public int delete(Long id) {
        String sql = "DELETE FROM post_images WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int deleteByPostId(Long postId) {
        String sql = "DELETE FROM post_images WHERE post_id = ?";
        return jdbcTemplate.update(sql, postId);
    }

    public int countByPostId(Long postId) {
        String sql = "SELECT COUNT(*) FROM post_images WHERE post_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, postId);
        return count != null ? count : 0;
    }

    public PostImage findFirstByPostId(Long postId) {
        String sql = "SELECT * FROM post_images WHERE post_id = ? ORDER BY sort_order ASC, created_at ASC LIMIT 1";
        List<PostImage> images = jdbcTemplate.query(sql, postImageRowMapper, postId);
        return images.isEmpty() ? null : images.get(0);
    }
}

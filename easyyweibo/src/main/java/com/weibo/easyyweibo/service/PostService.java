package com.weibo.easyyweibo.service;

import com.weibo.easyyweibo.dao.PostDao;
import com.weibo.easyyweibo.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostDao postDao;

    @Autowired
    private UserService userService;

    public Post getPostById(Long id) {
        return postDao.findById(id);
    }

    public List<Post> getAllPosts() {
        return postDao.findAll();
    }

    public List<Post> getPostsByUserId(Long userId) {
        return postDao.findByUserId(userId);
    }

    public List<Post> getPublicPosts() {
        return postDao.findPublicPosts();
    }

    public List<Post> getPostsByVisibility(Integer visibility) {
        return postDao.findByVisibility(visibility);
    }

    public Post createPost(Post post) {
        Long postId = postDao.insert(post);
        if (postId > 0) {
            userService.addPost(post.getUserId());
            post.setId(postId);
            return post;
        }
        return null;
    }

    public boolean updatePost(Post post) {
        return postDao.update(post) > 0;
    }

    public boolean deletePost(Long id, Long userId) {
        Post post = postDao.findById(id);
        if (post != null && post.getUserId().equals(userId)) {
            int result = postDao.delete(id);
            if (result > 0) {
                userService.removePost(userId);
                return true;
            }
        }
        return false;
    }

    public boolean likePost(Long id) {
        return postDao.incrementLikesCount(id) > 0;
    }

    public boolean unlikePost(Long id) {
        return postDao.decrementLikesCount(id) > 0;
    }

    public boolean addComment(Long postId) {
        return postDao.incrementCommentsCount(postId) > 0;
    }

    public boolean removeComment(Long postId) {
        return postDao.decrementCommentsCount(postId) > 0;
    }

    public int getPostCountByUser(Long userId) {
        return postDao.countByUserId(userId);
    }

    public List<Post> searchPosts(String keyword) {
        return postDao.findByContentContaining(keyword);
    }

    public int getUserTotalLikes(Long userId) {
        return postDao.sumLikesCountByUserId(userId);
    }
}

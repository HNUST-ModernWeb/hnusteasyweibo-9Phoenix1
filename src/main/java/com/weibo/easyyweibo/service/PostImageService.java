package com.weibo.easyyweibo.service;

import com.weibo.easyyweibo.dao.PostImageDao;
import com.weibo.easyyweibo.entity.PostImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostImageService {

    @Autowired
    private PostImageDao postImageDao;

    public PostImage getImageById(Long id) {
        return postImageDao.findById(id);
    }

    public List<PostImage> getImagesByPostId(Long postId) {
        return postImageDao.findByPostId(postId);
    }

    public List<PostImage> getAllImages() {
        return postImageDao.findAll();
    }

    public boolean addImage(PostImage postImage) {
        return postImageDao.insert(postImage) > 0;
    }

    public boolean addImages(Long postId, List<String> imageUrls) {
        for (int i = 0; i < imageUrls.size(); i++) {
            PostImage postImage = new PostImage();
            postImage.setPostId(postId);
            postImage.setImageUrl(imageUrls.get(i));
            postImage.setSortOrder(i);
            if (postImageDao.insert(postImage) <= 0) {
                return false;
            }
        }
        return true;
    }

    public boolean updateImage(PostImage postImage) {
        return postImageDao.update(postImage) > 0;
    }

    public boolean updateSortOrder(Long id, Integer sortOrder) {
        return postImageDao.updateSortOrder(id, sortOrder) > 0;
    }

    public boolean deleteImage(Long id) {
        return postImageDao.delete(id) > 0;
    }

    public boolean deleteImagesByPostId(Long postId) {
        return postImageDao.deleteByPostId(postId) > 0;
    }

    public int getImageCountByPost(Long postId) {
        return postImageDao.countByPostId(postId);
    }

    public PostImage getFirstImageByPost(Long postId) {
        return postImageDao.findFirstByPostId(postId);
    }
}

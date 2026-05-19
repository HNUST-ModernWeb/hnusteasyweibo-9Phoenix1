package com.weibo.easyyweibo.service;

import com.weibo.easyyweibo.dao.PostTagDao;
import com.weibo.easyyweibo.dao.TagDao;
import com.weibo.easyyweibo.entity.PostTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostTagService {

    @Autowired
    private PostTagDao postTagDao;

    @Autowired
    private TagDao tagDao;

    public PostTag getPostTagById(Long id) {
        return postTagDao.findById(id);
    }

    public List<PostTag> getPostTagsByPostId(Long postId) {
        return postTagDao.findByPostId(postId);
    }

    public List<PostTag> getPostTagsByTagId(Long tagId) {
        return postTagDao.findByTagId(tagId);
    }

    public List<PostTag> getAllPostTags() {
        return postTagDao.findAll();
    }

    @Transactional
    public boolean addTagToPost(Long postId, Long tagId) {
        if (postTagDao.existsByPostIdAndTagId(postId, tagId)) {
            return false;
        }
        PostTag postTag = new PostTag();
        postTag.setPostId(postId);
        postTag.setTagId(tagId);
        int result = postTagDao.insert(postTag);
        if (result > 0) {
            tagDao.incrementUsageCount(tagId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean removeTagFromPost(Long postId, Long tagId) {
        int result = postTagDao.deleteByPostIdAndTagId(postId, tagId);
        if (result > 0) {
            tagDao.decrementUsageCount(tagId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean removeAllTagsFromPost(Long postId) {
        List<PostTag> postTags = postTagDao.findByPostId(postId);
        int result = postTagDao.deleteByPostId(postId);
        if (result > 0) {
            for (PostTag postTag : postTags) {
                tagDao.decrementUsageCount(postTag.getTagId());
            }
            return true;
        }
        return false;
    }

    public boolean checkTagExistsInPost(Long postId, Long tagId) {
        return postTagDao.existsByPostIdAndTagId(postId, tagId);
    }

    public int getTagCountByPost(Long postId) {
        return postTagDao.countByPostId(postId);
    }

    public int getPostCountByTag(Long tagId) {
        return postTagDao.countByTagId(tagId);
    }
}

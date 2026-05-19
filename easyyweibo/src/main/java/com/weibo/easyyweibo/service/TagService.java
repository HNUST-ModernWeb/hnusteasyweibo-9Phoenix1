package com.weibo.easyyweibo.service;

import com.weibo.easyyweibo.dao.TagDao;
import com.weibo.easyyweibo.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagDao tagDao;

    public Tag getTagById(Long id) {
        return tagDao.findById(id);
    }

    public Tag getTagByName(String name) {
        return tagDao.findByName(name);
    }

    public List<Tag> getAllTags() {
        return tagDao.findAll();
    }

    public List<Tag> getPopularTags(int limit) {
        return tagDao.findPopularTags(limit);
    }

    public boolean createTag(Tag tag) {
        if (tagDao.existsByName(tag.getName())) {
            return false;
        }
        return tagDao.insert(tag) > 0;
    }

    public boolean updateTag(Tag tag) {
        return tagDao.update(tag) > 0;
    }

    public boolean deleteTag(Long id) {
        return tagDao.delete(id) > 0;
    }

    public boolean useTag(Long id) {
        return tagDao.incrementUsageCount(id) > 0;
    }

    public boolean unuseTag(Long id) {
        return tagDao.decrementUsageCount(id) > 0;
    }

    public boolean checkTagExists(String name) {
        return tagDao.existsByName(name);
    }
}

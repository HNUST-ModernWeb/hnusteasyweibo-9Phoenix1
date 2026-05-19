package com.weibo.easyyweibo.service;

import com.weibo.easyyweibo.dao.FollowDao;
import com.weibo.easyyweibo.dao.UserDao;
import com.weibo.easyyweibo.entity.Follow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowService {

    @Autowired
    private FollowDao followDao;

    @Autowired
    private UserDao userDao;

    public Follow getFollowById(Long id) {
        return followDao.findById(id);
    }

    public List<Follow> getAllFollows() {
        return followDao.findAll();
    }

    public List<Follow> getFollowsByFollowerId(Long followerId) {
        return followDao.findByFollowerId(followerId);
    }

    public List<Follow> getFollowsByFollowingId(Long followingId) {
        return followDao.findByFollowingId(followingId);
    }

    @Transactional
    public boolean followUser(Long followerId, Long followingId) {
        if (followDao.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            return false;
        }
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followingId);
        int result = followDao.insert(follow);
        if (result > 0) {
            userDao.incrementFollowingCount(followerId);
            userDao.incrementFollowersCount(followingId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean unfollowUser(Long followerId, Long followingId) {
        int result = followDao.deleteByFollowerIdAndFollowingId(followerId, followingId);
        if (result > 0) {
            userDao.decrementFollowingCount(followerId);
            userDao.decrementFollowersCount(followingId);
            return true;
        }
        return false;
    }

    public boolean checkIsFollowing(Long followerId, Long followingId) {
        return followDao.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    public int getFollowingCount(Long followerId) {
        return followDao.countByFollowerId(followerId);
    }

    public int getFollowersCount(Long followingId) {
        return followDao.countByFollowingId(followingId);
    }
}

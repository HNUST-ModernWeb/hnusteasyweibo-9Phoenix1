package com.weibo.easyyweibo.service;

import com.weibo.easyyweibo.dao.UserDao;
import com.weibo.easyyweibo.entity.User;
import com.weibo.easyyweibo.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    public User getUserByPhone(String phone) {
        return userDao.findByPhone(phone);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public List<User> getUsersByStatus(Integer status) {
        return userDao.findByStatus(status);
    }

    public boolean register(User user) {
        if (userDao.existsByPhone(user.getPhone())) {
            return false;
        }
        if (user.getEmail() != null && userDao.existsByEmail(user.getEmail())) {
            return false;
        }
        // 对密码进行加密
        user.setPassword(PasswordUtil.encode(user.getPassword()));
        return userDao.insert(user) > 0;
    }

    public boolean updateUser(User user) {
        return userDao.update(user) > 0;
    }

    public boolean updatePassword(Long id, String password) {
        // 对新密码进行加密
        String encodedPassword = PasswordUtil.encode(password);
        return userDao.updatePassword(id, encodedPassword) > 0;
    }

    public boolean updateLastLogin(Long id) {
        return userDao.updateLastLogin(id) > 0;
    }

    public boolean deleteUser(Long id) {
        return userDao.delete(id) > 0;
    }

    public boolean followUser(Long userId, Long targetUserId) {
        userDao.incrementFollowingCount(userId);
        userDao.incrementFollowersCount(targetUserId);
        return true;
    }

    public boolean unfollowUser(Long userId, Long targetUserId) {
        userDao.decrementFollowingCount(userId);
        userDao.decrementFollowersCount(targetUserId);
        return true;
    }

    public boolean addPost(Long userId) {
        return userDao.incrementPostsCount(userId) > 0;
    }

    public boolean removePost(Long userId) {
        return userDao.decrementPostsCount(userId) > 0;
    }

    public boolean checkPhoneExists(String phone) {
        return userDao.existsByPhone(phone);
    }

    public boolean checkEmailExists(String email) {
        return userDao.existsByEmail(email);
    }

    public List<User> getUnfollowedUsers(Long userId, int limit) {
        return userDao.findUnfollowedUsers(userId, limit);
    }
}

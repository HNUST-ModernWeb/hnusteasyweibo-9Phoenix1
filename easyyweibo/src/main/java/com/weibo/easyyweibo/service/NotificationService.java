package com.weibo.easyyweibo.service;

import com.weibo.easyyweibo.dao.NotificationDao;
import com.weibo.easyyweibo.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationDao notificationDao;

    public Notification getNotificationById(Long id) {
        return notificationDao.findById(id);
    }

    public List<Notification> getAllNotifications() {
        return notificationDao.findAll();
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationDao.findByUserId(userId);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationDao.findUnreadByUserId(userId);
    }

    public List<Notification> getNotificationsByType(Long userId, Integer type) {
        return notificationDao.findByUserIdAndType(userId, type);
    }

    public boolean createNotification(Notification notification) {
        return notificationDao.insert(notification) > 0;
    }

    public boolean markAsRead(Long id) {
        return notificationDao.markAsRead(id) > 0;
    }

    public boolean markAllAsRead(Long userId) {
        return notificationDao.markAllAsReadByUserId(userId) > 0;
    }

    public boolean deleteNotification(Long id) {
        return notificationDao.delete(id) > 0;
    }

    public boolean deleteAllNotifications(Long userId) {
        return notificationDao.deleteByUserId(userId) >= 0;
    }

    public int getUnreadCount(Long userId) {
        return notificationDao.countUnreadByUserId(userId);
    }

    public int getNotificationCount(Long userId) {
        return notificationDao.countByUserId(userId);
    }

    public boolean createLikeNotification(Long userId, Long senderId, Long postId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setSenderId(senderId);
        notification.setType(1);
        notification.setPostId(postId);
        notification.setContent("赞了你的动态");
        return createNotification(notification);
    }

    public boolean createCommentNotification(Long userId, Long senderId, Long postId, Long commentId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setSenderId(senderId);
        notification.setType(2);
        notification.setPostId(postId);
        notification.setCommentId(commentId);
        notification.setContent("评论了你的动态");
        return createNotification(notification);
    }

    public boolean createFollowNotification(Long userId, Long senderId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setSenderId(senderId);
        notification.setType(3);
        notification.setContent("关注了你");
        return createNotification(notification);
    }

    public boolean createSystemNotification(Long userId, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(4);
        notification.setContent(content);
        return createNotification(notification);
    }
}

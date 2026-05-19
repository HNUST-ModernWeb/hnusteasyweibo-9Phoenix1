-- ============================================
-- EasyWeibo 数据库表结构 + 测试数据 (手机号登录版)
-- 数据库名: easyweibo
-- 修改: username -> phone 作为登录账号
-- ============================================

-- 使用数据库
USE easyweibo;

-- 禁用外键检查，方便删除表
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 删除现有表（按依赖顺序）
-- ============================================
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS follows;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS post_tags;
DROP TABLE IF EXISTS post_images;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS users;

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 1. 用户表 (users) - 使用手机号作为登录账号
-- ============================================
CREATE TABLE users (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号(登录账号)',
    password VARCHAR(255) NOT NULL COMMENT '密码(加密存储)',
    nickname VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    avatar VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    bio VARCHAR(255) DEFAULT NULL COMMENT '个人简介',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
    followers_count INT UNSIGNED DEFAULT 0 COMMENT '粉丝数',
    following_count INT UNSIGNED DEFAULT 0 COMMENT '关注数',
    posts_count INT UNSIGNED DEFAULT 0 COMMENT '动态数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_at TIMESTAMP NULL DEFAULT NULL COMMENT '最后登录时间',
    INDEX idx_phone (phone),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 2. 动态表 (posts)
-- ============================================
CREATE TABLE posts (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '动态ID',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '发布者ID',
    content TEXT NOT NULL COMMENT '动态内容',
    visibility TINYINT DEFAULT 1 COMMENT '可见性: 0-私密, 1-公开, 2-粉丝可见',
    likes_count INT UNSIGNED DEFAULT 0 COMMENT '点赞数',
    comments_count INT UNSIGNED DEFAULT 0 COMMENT '评论数',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-删除, 1-正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_visibility (visibility),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动态表';

-- ============================================
-- 3. 动态图片表 (post_images)
-- ============================================
CREATE TABLE post_images (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '图片ID',
    post_id BIGINT UNSIGNED NOT NULL COMMENT '动态ID',
    image_url TEXT NOT NULL COMMENT '图片URL或Base64数据',
    sort_order INT UNSIGNED DEFAULT 0 COMMENT '排序顺序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    INDEX idx_post_id (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动态图片表';

-- ============================================
-- 4. 标签表 (tags)
-- ============================================
CREATE TABLE tags (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '标签ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名称',
    usage_count INT UNSIGNED DEFAULT 0 COMMENT '使用次数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_name (name),
    INDEX idx_usage_count (usage_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- ============================================
-- 5. 动态标签关联表 (post_tags)
-- ============================================
CREATE TABLE post_tags (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    post_id BIGINT UNSIGNED NOT NULL COMMENT '动态ID',
    tag_id BIGINT UNSIGNED NOT NULL COMMENT '标签ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE,
    UNIQUE KEY uk_post_tag (post_id, tag_id),
    INDEX idx_post_id (post_id),
    INDEX idx_tag_id (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动态标签关联表';

-- ============================================
-- 6. 点赞表 (likes)
-- ============================================
CREATE TABLE likes (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '点赞ID',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    post_id BIGINT UNSIGNED NOT NULL COMMENT '动态ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_post (user_id, post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_post_id (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='点赞表';

-- ============================================
-- 7. 评论表 (comments)
-- ============================================
CREATE TABLE comments (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    post_id BIGINT UNSIGNED NOT NULL COMMENT '动态ID',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '评论者ID',
    parent_id BIGINT UNSIGNED DEFAULT NULL COMMENT '父评论ID(回复功能)',
    content TEXT NOT NULL COMMENT '评论内容',
    likes_count INT UNSIGNED DEFAULT 0 COMMENT '点赞数',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-删除, 1-正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES comments(id) ON DELETE CASCADE,
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- ============================================
-- 8. 关注关系表 (follows)
-- ============================================
CREATE TABLE follows (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    follower_id BIGINT UNSIGNED NOT NULL COMMENT '关注者ID',
    following_id BIGINT UNSIGNED NOT NULL COMMENT '被关注者ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_follow (follower_id, following_id),
    INDEX idx_follower (follower_id),
    INDEX idx_following (following_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关注关系表';

-- ============================================
-- 9. 通知表 (notifications)
-- ============================================
CREATE TABLE notifications (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '通知ID',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '接收者ID',
    sender_id BIGINT UNSIGNED DEFAULT NULL COMMENT '发送者ID',
    type TINYINT NOT NULL COMMENT '类型: 1-点赞, 2-评论, 3-关注, 4-系统',
    post_id BIGINT UNSIGNED DEFAULT NULL COMMENT '相关动态ID',
    comment_id BIGINT UNSIGNED DEFAULT NULL COMMENT '相关评论ID',
    content VARCHAR(255) DEFAULT NULL COMMENT '通知内容',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读: 0-未读, 1-已读',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知表';

-- ============================================
-- 插入测试数据 (使用手机号作为账号)
-- ============================================

-- 1. 用户数据 (密码: 123456)
INSERT INTO users (id, phone, password, nickname, avatar, bio, email, followers_count, following_count, posts_count) VALUES
(1, '13800138000', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '管理员', 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin', '系统管理员', 'admin@easyweibo.com', 0, 0, 0),
(2, '13800138001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '分享达人', 'https://api.dicebear.com/7.x/avataaars/svg?seed=user1', '热爱生活，喜欢分享', 'user1@example.com', 128, 56, 5),
(3, '13800138002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '设计师小王', 'https://api.dicebear.com/7.x/avataaars/svg?seed=designer', '专注设计10年', 'designer@example.com', 256, 89, 3),
(4, '13800138003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '摄影师李明', 'https://api.dicebear.com/7.x/avataaars/svg?seed=photo', '用镜头记录美好', 'photo@example.com', 512, 123, 4),
(5, '13800138004', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '美食探店', 'https://api.dicebear.com/7.x/avataaars/svg?seed=food', '吃货一枚，专注美食', 'food@example.com', 189, 67, 2),
(6, '13800138005', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '程序员小张', 'https://api.dicebear.com/7.x/avataaars/svg?seed=coder', '代码改变世界', 'coder@example.com', 89, 45, 2),
(7, '13800138006', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '读书君', 'https://api.dicebear.com/7.x/avataaars/svg?seed=book', '书中自有黄金屋', 'book@example.com', 234, 156, 3),
(8, '13800138007', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '旅行达人', 'https://api.dicebear.com/7.x/avataaars/svg?seed=travel', '世界那么大，我想去看看', 'travel@example.com', 345, 89, 4),
(9, '13800138008', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '咖啡师阿杰', 'https://api.dicebear.com/7.x/avataaars/svg?seed=coffee', '咖啡重度爱好者', 'coffee@example.com', 156, 78, 2);

-- 2. 标签数据
INSERT INTO tags (id, name, usage_count) VALUES
(1, '设计', 15),
(2, '创意', 8),
(3, '摄影', 23),
(4, '风景', 18),
(5, '旅行', 12),
(6, '美食', 20),
(7, '探店', 6),
(8, '编程', 9),
(9, '生活', 25),
(10, '读书', 11),
(11, '文学', 7),
(12, '推荐', 5),
(13, '咖啡', 8),
(14, '技术', 6),
(15, '日常', 15);

-- 3. 动态数据
INSERT INTO posts (id, user_id, content, visibility, likes_count, comments_count, created_at) VALUES
(1, 3, '今天设计了一个新的Logo，感觉还不错！简约风格真的永不过时。大家觉得呢？', 1, 24, 2, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(2, 4, '周末去山里拍的风景，大自然的色彩真的太美了。每一帧都是壁纸级别！', 1, 156, 3, DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(3, 5, '发现一家超棒的日料店，刺身新鲜到爆！推荐他们家的三文鱼和北极贝。', 1, 89, 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(4, 6, '终于解决了困扰我三天的bug！原来是一个小小的拼写错误。编程就是这样，细节决定成败。', 1, 45, 2, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(5, 7, '刚读完《百年孤独》，马尔克斯的魔幻现实主义真的太震撼了。那种孤独感贯穿全书，让人久久不能平静。强烈推荐！', 1, 234, 3, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(6, 8, '日本京都的樱花季真的太美了！满城的粉色，仿佛置身童话世界。', 1, 312, 2, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(7, 9, '今天学会了拉花，虽然还不够完美，但已经能看出是个心形了！', 1, 67, 0, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(8, 2, '分享一个今天看到的好句子：生活不是等待暴风雨过去，而是学会在雨中跳舞。', 1, 128, 5, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(9, 3, '新做的UI界面，采用了玻璃拟态设计风格，大家觉得怎么样？', 1, 89, 4, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(10, 4, '日落时分的海边，金色的阳光洒在海面上，美得让人窒息。', 1, 267, 6, DATE_SUB(NOW(), INTERVAL 8 HOUR)),
(11, 5, '这家火锅店的毛肚真的太脆了，配上秘制蘸料，绝配！', 1, 156, 3, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(12, 6, 'Vue3 + Spring Boot 前后端分离项目终于完成了，学到了很多东西！', 1, 78, 4, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(13, 7, '《三体》真的太好看了，刘慈欣的想象力简直突破天际！', 1, 189, 5, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(14, 8, '西藏的星空，没有光污染的地方，银河清晰可见。', 1, 423, 8, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(15, 2, '今天天气真好，适合出去走走，晒晒太阳。', 1, 56, 1, DATE_SUB(NOW(), INTERVAL 1 HOUR));

-- 4. 动态图片数据
INSERT INTO post_images (post_id, image_url, sort_order) VALUES
(1, 'https://picsum.photos/seed/logo1/600/400', 0),
(1, 'https://picsum.photos/seed/logo2/600/400', 1),
(2, 'https://picsum.photos/seed/mountain/600/800', 0),
(2, 'https://picsum.photos/seed/forest/600/400', 1),
(2, 'https://picsum.photos/seed/river/600/400', 2),
(3, 'https://picsum.photos/seed/sushi/600/600', 0),
(5, 'https://picsum.photos/seed/book/600/800', 0),
(6, 'https://picsum.photos/seed/kyoto/600/400', 0),
(6, 'https://picsum.photos/seed/sakura/600/600', 1),
(7, 'https://picsum.photos/seed/coffee/600/600', 0),
(9, 'https://picsum.photos/seed/ui1/600/400', 0),
(9, 'https://picsum.photos/seed/ui2/600/400', 1),
(10, 'https://picsum.photos/seed/sunset/600/400', 0),
(10, 'https://picsum.photos/seed/beach/600/600', 1),
(10, 'https://picsum.photos/seed/ocean/600/400', 2),
(11, 'https://picsum.photos/seed/hotpot/600/600', 0),
(14, 'https://picsum.photos/seed/stars/600/400', 0),
(14, 'https://picsum.photos/seed/galaxy/600/600', 1),
(14, 'https://picsum.photos/seed/night/600/400', 2);

-- 5. 动态标签关联数据
INSERT INTO post_tags (post_id, tag_id) VALUES
(1, 1), (1, 2),
(2, 3), (2, 4), (2, 5),
(3, 6), (3, 7),
(4, 8), (4, 9),
(5, 10), (5, 11), (5, 12),
(6, 3), (6, 4), (6, 5),
(7, 6), (7, 9),
(8, 9), (8, 15),
(9, 1), (9, 2),
(10, 3), (10, 4),
(11, 6), (11, 7),
(12, 8), (12, 14),
(13, 10), (13, 11),
(14, 3), (14, 4), (14, 5),
(15, 9), (15, 15);

-- 6. 评论数据
INSERT INTO comments (id, post_id, user_id, content, likes_count, created_at) VALUES
(1, 1, 2, '真的很好看！简约而不简单', 5, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(2, 1, 4, '配色很舒服，学习了', 3, DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(3, 2, 5, '这是哪里啊？求地址', 8, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(4, 2, 4, '在黄山拍的，风景确实美', 12, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(5, 2, 6, '想去！', 2, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(6, 3, 2, '看着就好吃', 4, DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(7, 4, 3, '感同身受！bug总是藏在细节里', 6, DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(8, 4, 7, 'debug是程序员的家常便饭', 3, DATE_SUB(NOW(), INTERVAL 18 HOUR)),
(9, 5, 2, '我也刚读完，确实经典', 15, DATE_SUB(NOW(), INTERVAL 40 HOUR)),
(10, 5, 6, '马尔克斯的其他作品也很棒', 8, DATE_SUB(NOW(), INTERVAL 38 HOUR)),
(11, 5, 8, '加入书单了', 5, DATE_SUB(NOW(), INTERVAL 36 HOUR)),
(12, 6, 2, '好想去！', 10, DATE_SUB(NOW(), INTERVAL 50 HOUR)),
(13, 6, 9, '明年樱花季一定要去', 7, DATE_SUB(NOW(), INTERVAL 48 HOUR)),
(14, 8, 3, '说得好！', 3, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(15, 8, 4, '收藏了', 2, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(16, 9, 2, '玻璃拟态现在很流行', 6, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(17, 10, 5, '太美了！', 12, DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(18, 12, 2, '求源码学习', 5, DATE_SUB(NOW(), INTERVAL 10 HOUR)),
(19, 13, 2, '黑暗森林法则让人细思极恐', 18, DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(20, 14, 2, '这辈子一定要去一次西藏', 25, DATE_SUB(NOW(), INTERVAL 80 HOUR));

-- 7. 点赞数据
INSERT INTO likes (user_id, post_id, created_at) VALUES
(2, 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(2, 2, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(2, 5, DATE_SUB(NOW(), INTERVAL 40 HOUR)),
(2, 6, DATE_SUB(NOW(), INTERVAL 50 HOUR)),
(2, 10, DATE_SUB(NOW(), INTERVAL 7 HOUR)),
(2, 14, DATE_SUB(NOW(), INTERVAL 80 HOUR)),
(3, 2, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(3, 4, DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(3, 8, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(4, 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(4, 5, DATE_SUB(NOW(), INTERVAL 38 HOUR)),
(4, 9, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(5, 2, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(5, 6, DATE_SUB(NOW(), INTERVAL 48 HOUR)),
(5, 10, DATE_SUB(NOW(), INTERVAL 7 HOUR)),
(6, 3, DATE_SUB(NOW(), INTERVAL 22 HOUR)),
(6, 7, DATE_SUB(NOW(), INTERVAL 50 HOUR)),
(7, 4, DATE_SUB(NOW(), INTERVAL 18 HOUR)),
(7, 12, DATE_SUB(NOW(), INTERVAL 10 HOUR)),
(8, 5, DATE_SUB(NOW(), INTERVAL 36 HOUR)),
(8, 11, DATE_SUB(NOW(), INTERVAL 40 HOUR)),
(9, 6, DATE_SUB(NOW(), INTERVAL 48 HOUR)),
(9, 14, DATE_SUB(NOW(), INTERVAL 82 HOUR));

-- 8. 关注关系数据
INSERT INTO follows (follower_id, following_id, created_at) VALUES
(2, 3, DATE_SUB(NOW(), INTERVAL 30 DAY)),
(2, 4, DATE_SUB(NOW(), INTERVAL 25 DAY)),
(2, 5, DATE_SUB(NOW(), INTERVAL 20 DAY)),
(2, 7, DATE_SUB(NOW(), INTERVAL 15 DAY)),
(2, 8, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(3, 2, DATE_SUB(NOW(), INTERVAL 28 DAY)),
(3, 4, DATE_SUB(NOW(), INTERVAL 22 DAY)),
(4, 2, DATE_SUB(NOW(), INTERVAL 26 DAY)),
(4, 3, DATE_SUB(NOW(), INTERVAL 24 DAY)),
(4, 8, DATE_SUB(NOW(), INTERVAL 18 DAY)),
(5, 2, DATE_SUB(NOW(), INTERVAL 21 DAY)),
(5, 6, DATE_SUB(NOW(), INTERVAL 16 DAY)),
(6, 2, DATE_SUB(NOW(), INTERVAL 19 DAY)),
(6, 4, DATE_SUB(NOW(), INTERVAL 14 DAY)),
(7, 2, DATE_SUB(NOW(), INTERVAL 17 DAY)),
(7, 5, DATE_SUB(NOW(), INTERVAL 12 DAY)),
(8, 2, DATE_SUB(NOW(), INTERVAL 15 DAY)),
(8, 4, DATE_SUB(NOW(), INTERVAL 11 DAY)),
(9, 2, DATE_SUB(NOW(), INTERVAL 13 DAY)),
(9, 7, DATE_SUB(NOW(), INTERVAL 8 DAY));

-- 9. 通知数据
INSERT INTO notifications (user_id, sender_id, type, post_id, content, is_read, created_at) VALUES
(2, 3, 2, 1, '设计师小王 评论了你的动态', 0, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(2, 4, 2, 2, '摄影师李明 评论了你的动态', 0, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(2, 5, 2, 2, '美食探店 评论了你的动态', 0, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(2, 6, 1, 8, '程序员小张 赞了你的动态', 1, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(2, 7, 1, 8, '读书君 赞了你的动态', 0, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(3, 2, 1, 1, '分享达人 赞了你的动态', 0, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(4, 2, 1, 2, '分享达人 赞了你的动态', 1, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(5, 2, 1, 3, '分享达人 赞了你的动态', 0, DATE_SUB(NOW(), INTERVAL 22 HOUR)),
(6, 2, 3, NULL, '分享达人 关注了你', 0, DATE_SUB(NOW(), INTERVAL 19 DAY)),
(7, 2, 3, NULL, '分享达人 关注了你', 1, DATE_SUB(NOW(), INTERVAL 17 DAY));

-- ============================================
-- 查看数据统计
-- ============================================
SELECT 'users' as table_name, COUNT(*) as count FROM users
UNION ALL
SELECT 'posts', COUNT(*) FROM posts
UNION ALL
SELECT 'post_images', COUNT(*) FROM post_images
UNION ALL
SELECT 'tags', COUNT(*) FROM tags
UNION ALL
SELECT 'post_tags', COUNT(*) FROM post_tags
UNION ALL
SELECT 'likes', COUNT(*) FROM likes
UNION ALL
SELECT 'comments', COUNT(*) FROM comments
UNION ALL
SELECT 'follows', COUNT(*) FROM follows
UNION ALL
SELECT 'notifications', COUNT(*) FROM notifications;

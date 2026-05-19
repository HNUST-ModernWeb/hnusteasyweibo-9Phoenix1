-- 修改 post_images 表的 image_url 字段为 TEXT 类型，支持存储 Base64 图片
ALTER TABLE post_images MODIFY COLUMN image_url TEXT NOT NULL COMMENT '图片URL或Base64数据';

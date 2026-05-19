-- 修改 avatar 字段类型，支持存储 Base64 图片数据
ALTER TABLE users MODIFY COLUMN avatar TEXT COMMENT '头像URL或Base64数据';

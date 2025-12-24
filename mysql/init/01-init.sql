-- 浮云导航数据库初始化脚本
-- 创建数据库和用户

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `github_id` varchar(100) NOT NULL COMMENT 'GitHub用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar_url` varchar(500) DEFAULT NULL COMMENT '头像URL',
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_github_id` (`github_id`),
  KEY `idx_username` (`username`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 导航分类表
CREATE TABLE IF NOT EXISTS `navigation_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `description` text COMMENT '分类描述',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `sort_order` int DEFAULT 0 COMMENT '排序顺序',
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='导航分类表';

-- 导航链接表
CREATE TABLE IF NOT EXISTS `navigation_link` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '链接ID',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `title` varchar(100) NOT NULL COMMENT '链接标题',
  `description` text COMMENT '链接描述',
  `url` varchar(500) NOT NULL COMMENT '链接URL',
  `icon` varchar(200) DEFAULT NULL COMMENT '图标URL',
  `sort_order` int DEFAULT 0 COMMENT '排序顺序',
  `is_active` tinyint(1) DEFAULT 1 COMMENT '是否激活',
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_sort_order` (`sort_order`),
  KEY `idx_is_active` (`is_active`),
  CONSTRAINT `fk_navigation_link_category` FOREIGN KEY (`category_id`) REFERENCES `navigation_category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='导航链接表';

-- 插入默认数据
INSERT IGNORE INTO `navigation_category` (`id`, `name`, `description`, `icon`, `sort_order`) VALUES
(1, '常用工具', '日常开发和工作中常用的工具网站', 'build', 1),
(2, '技术资源', '编程学习和技术文档资源', 'code', 2),
(3, '设计资源', 'UI设计和创意灵感资源', 'palette', 3),
(4, '娱乐休闲', '放松身心的娱乐网站', 'movie', 4),
(5, '学习提升', '在线学习和知识获取平台', 'school', 5);

INSERT IGNORE INTO `navigation_link` (`id`, `category_id`, `title`, `description`, `url`, `icon`, `sort_order`, `is_active`) VALUES
-- 常用工具
(1, 1, 'GitHub', '全球最大的代码托管平台', 'https://github.com', 'fab fa-github', 1, 1),
(2, 1, 'Stack Overflow', '程序员问答社区', 'https://stackoverflow.com', 'fab fa-stack-overflow', 2, 1),
(3, 1, 'MDN Web Docs', 'Web技术文档', 'https://developer.mozilla.org', 'fas fa-book', 3, 1),
(4, 1, 'VS Code', '微软开发的代码编辑器', 'https://code.visualstudio.com', 'fas fa-code', 4, 1),

-- 技术资源
(5, 2, 'Vue.js', '渐进式JavaScript框架', 'https://vuejs.org', 'fab fa-vuejs', 1, 1),
(6, 2, 'Spring Boot', 'Java应用开发框架', 'https://spring.io/projects/spring-boot', 'fas fa-leaf', 2, 1),
(7, 2, 'Docker Hub', 'Docker镜像仓库', 'https://hub.docker.com', 'fab fa-docker', 3, 1),
(8, 2, '掘金', '技术文章分享社区', 'https://juejin.cn', 'fas fa-pen', 4, 1),

-- 设计资源
(9, 3, 'Dribbble', '设计师作品展示平台', 'https://dribbble.com', 'fab fa-dribbble', 1, 1),
(10, 3, 'Behance', 'Adobe创意作品展示', 'https://www.behance.net', 'fab fa-behance', 2, 1),
(11, 3, 'Figma', '协作式设计工具', 'https://www.figma.com', 'fab fa-figma', 3, 1),
(12, 3, 'Unsplash', '免费高质量图片', 'https://unsplash.com', 'fas fa-image', 4, 1),

-- 娱乐休闲
(13, 4, 'Bilibili', '年轻人喜欢的视频网站', 'https://www.bilibili.com', 'fas fa-play', 1, 1),
(14, 4, '网易云音乐', '音乐播放和发现平台', 'https://music.163.com', 'fas fa-music', 2, 1),
(15, 4, '豆瓣', '图书、电影、音乐评分社区', 'https://www.douban.com', 'fas fa-star', 3, 1),

-- 学习提升
(16, 5, 'Coursera', '在线课程学习平台', 'https://www.coursera.org', 'fas fa-graduation-cap', 1, 1),
(17, 5, '极客时间', '技术知识学习平台', 'https://time.geekbang.org', 'fas fa-clock', 2, 1),
(18, 5, 'InfoQ', '技术媒体和知识分享', 'https://www.infoq.cn', 'fas fa-info-circle', 3, 1);

SET FOREIGN_KEY_CHECKS = 1;
CREATE DATABASE IF NOT EXISTS myappdb;

USE myappdb;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    imgSrc VARCHAR(255) DEFAULT 'https://api.dicebear.com/7.x/avataaars/svg?seed=',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建玩家信息表
CREATE TABLE IF NOT EXISTS player_profile (
    user_id BIGINT PRIMARY KEY,
    userLevel VARCHAR(50) DEFAULT '无等级',
    win INT DEFAULT 0,
    speed DOUBLE DEFAULT 0.0,
    winRate DOUBLE DEFAULT 0.0,
    accuracyRate DOUBLE DEFAULT 0.0,
    rankScore INT DEFAULT 500,
    matchesPlayed INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 创建游戏好友表
CREATE TABLE IF NOT EXISTS user_friends (
    user_id BIGINT,
    friend_id BIGINT,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (friend_id) REFERENCES users(id)
);


-- 创建游戏文本表
CREATE TABLE IF NOT EXISTS game_texts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL COMMENT '文本内容',
    language VARCHAR(10) NOT NULL COMMENT '语言类型：CHINESE/ENGLISH',
    category VARCHAR(20) NOT NULL COMMENT '文本类型：DAILY_CHAT/ACADEMIC_WRITING/LATEX_MATH/PROGRAMMING等',
    difficulty VARCHAR(10) NOT NULL COMMENT '难度：EASY/MEDIUM/HARD',
    word_count INT NOT NULL COMMENT '字数统计',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    -- 添加索引以提高查询性能
    INDEX idx_language_category_difficulty (language, category, difficulty),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='打字游戏文本库';
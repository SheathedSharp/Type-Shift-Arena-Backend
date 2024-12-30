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
    user_level VARCHAR(50) DEFAULT '无等级',
    
    -- 排位赛数据
    ranked_matches_played INT DEFAULT 0,
    ranked_wins INT DEFAULT 0,
    ranked_win_rate DOUBLE DEFAULT 0.0,
    ranked_avg_wpm DOUBLE DEFAULT 0.0,
    ranked_avg_accuracy DOUBLE DEFAULT 0.0,
    rank_score INT DEFAULT 1200,
    
    -- 非排位赛数据
    casual_matches_played INT DEFAULT 0,
    casual_wins INT DEFAULT 0,
    casual_win_rate DOUBLE DEFAULT 0.0,
    casual_avg_wpm DOUBLE DEFAULT 0.0,
    casual_avg_accuracy DOUBLE DEFAULT 0.0,
    
    -- 总体数据
    total_matches_played INT DEFAULT 0,
    total_wins INT DEFAULT 0,
    highest_wpm DOUBLE DEFAULT 0.0,
    total_win_rate DOUBLE DEFAULT 0.0,
    total_avg_wpm DOUBLE DEFAULT 0.0,
    total_avg_accuracy DOUBLE DEFAULT 0.0,
    
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
    id CHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL COMMENT '文本标题',
    content TEXT NOT NULL COMMENT '文本内容',
    source_title VARCHAR(255) COMMENT '选自作品',
    source_author VARCHAR(255) COMMENT '作者',
    language VARCHAR(10) NOT NULL COMMENT '语言类型：CHINESE/ENGLISH',
    category VARCHAR(20) NOT NULL COMMENT '文本类型：DAILY_CHAT/ACADEMIC_WRITING/LATEX_MATH/PROGRAMMING/LITERATURE等',
    difficulty VARCHAR(10) NOT NULL COMMENT '难度：EASY/MEDIUM/HARD',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    is_custom BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否为自定义文本',
    
    -- 添加索引以提高查询性能
    INDEX idx_language_category_difficulty (language, category, difficulty),
    INDEX idx_created_at (created_at)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏文本库';


-- 创建游戏记录表
CREATE TABLE IF NOT EXISTS game_matches (
    id VARCHAR(36) PRIMARY KEY,
    room_id VARCHAR(255) NOT NULL,
    player1_id BIGINT NOT NULL,
    player2_id BIGINT NOT NULL,
    winner_id BIGINT,
    language VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    difficulty VARCHAR(50) NOT NULL,
    player1_wpm DOUBLE,
    player2_wpm DOUBLE,
    player1_accuracy DOUBLE,
    player2_accuracy DOUBLE,
    start_time BIGINT NOT NULL,
    end_time BIGINT NOT NULL,
    is_ranked BOOLEAN NOT NULL DEFAULT FALSE,
    target_text TEXT,
    FOREIGN KEY (player1_id) REFERENCES users(id),
    FOREIGN KEY (player2_id) REFERENCES users(id),
    FOREIGN KEY (winner_id) REFERENCES users(id)
);
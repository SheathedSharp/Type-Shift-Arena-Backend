CREATE DATABASE IF NOT EXISTS myappdb;

USE myappdb;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    imgSrc VARCHAR(255) DEFAULT 'https://api.dicebear.com/7.x/avataaars/svg?seed=',
    role VARCHAR(50) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建玩家信息表
CREATE TABLE IF NOT EXISTS player_profile (
    user_id VARCHAR(36) PRIMARY KEY,
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
    user_id VARCHAR(36),
    friend_id VARCHAR(36),
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
    player1_id VARCHAR(36) NOT NULL,
    player2_id VARCHAR(36) NOT NULL,
    winner_id VARCHAR(36),
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

-- 创建消息表
CREATE TABLE IF NOT EXISTS messages (
    id VARCHAR(36) PRIMARY KEY,
    sender_id VARCHAR(36) NOT NULL,
    receiver_id VARCHAR(36) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    content VARCHAR(500),
    created_at TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id)
);

-- 游戏模式表 (顶层)
CREATE TABLE IF NOT EXISTS game_modes (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '模式名称：如CLASSIC, BUBBLE, IDIOMGUASS',
    display_name VARCHAR(100) NOT NULL COMMENT '显示名称：如经典模式, 泡泡模式, 猜谜模式',
    description TEXT COMMENT '模式描述',
    min_players INT NOT NULL DEFAULT 2 COMMENT '最小玩家数',
    max_players INT NOT NULL DEFAULT 2 COMMENT '最大玩家数', 
    default_max_players INT NOT NULL DEFAULT 2 COMMENT '默认最大玩家数',
    player_options JSON COMMENT '可选人数选项（JSON数组格式）',
    duration INT COMMENT '游戏持续盘数',
    available_time_start TIME COMMENT '开放时间段-开始时间',
    available_time_end TIME COMMENT '开放时间段-结束时间',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否激活',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_is_active (is_active),
    INDEX idx_sort_order (sort_order),
    INDEX idx_min_max_players (min_players, max_players)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏模式配置表';

-- 游戏语言表
CREATE TABLE IF NOT EXISTS game_languages (
    id VARCHAR(36) PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE COMMENT '语言代码：如zh, en, ja, ko',
    name VARCHAR(50) NOT NULL COMMENT '语言名称：如CHINESE, ENGLISH, JAPANESE, KOREAN',
    display_name VARCHAR(100) NOT NULL COMMENT '显示名称：如简体中文, English, 日本語, 한국어',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否激活',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_code (code),
    INDEX idx_is_active (is_active),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏语言配置表';

-- 游戏类型表
CREATE TABLE IF NOT EXISTS game_categories (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '类型名称：如DAILY_CHAT, ACADEMIC_WRITING等',
    display_name VARCHAR(100) NOT NULL COMMENT '显示名称：如日常聊天, 学术写作等',
    description TEXT COMMENT '类型描述',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否激活',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_name (name),
    INDEX idx_is_active (is_active),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏类型配置表';

-- 游戏难度表 (底层)
CREATE TABLE IF NOT EXISTS game_difficulties (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '难度名称：如EASY, MEDIUM, HARD',
    display_name VARCHAR(100) NOT NULL COMMENT '显示名称：如简单, 中等, 困难',
    description TEXT COMMENT '难度描述',
    level_value INT NOT NULL COMMENT '难度数值，用于排序和比较',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否激活',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_name (name),
    INDEX idx_level_value (level_value),
    INDEX idx_is_active (is_active),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏难度配置表';

-- 多对多关系表

-- 游戏模式-语言关联表
CREATE TABLE IF NOT EXISTS game_mode_languages (
    id VARCHAR(36) PRIMARY KEY,
    mode_id VARCHAR(36) NOT NULL,
    language_id VARCHAR(36) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (mode_id) REFERENCES game_modes(id) ON DELETE CASCADE,
    FOREIGN KEY (language_id) REFERENCES game_languages(id) ON DELETE CASCADE,
    UNIQUE KEY uk_mode_language (mode_id, language_id),
    INDEX idx_mode_id (mode_id),
    INDEX idx_language_id (language_id),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏模式-语言关联表';

-- 游戏语言-类型关联表
CREATE TABLE IF NOT EXISTS game_language_categories (
    id VARCHAR(36) PRIMARY KEY,
    language_id VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (language_id) REFERENCES game_languages(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES game_categories(id) ON DELETE CASCADE,
    UNIQUE KEY uk_language_category (language_id, category_id),
    INDEX idx_language_id (language_id),
    INDEX idx_category_id (category_id),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏语言-类型关联表';

-- 游戏类型-难度关联表
CREATE TABLE IF NOT EXISTS game_category_difficulties (
    id VARCHAR(36) PRIMARY KEY,
    category_id VARCHAR(36) NOT NULL,
    difficulty_id VARCHAR(36) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (category_id) REFERENCES game_categories(id) ON DELETE CASCADE,
    FOREIGN KEY (difficulty_id) REFERENCES game_difficulties(id) ON DELETE CASCADE,
    UNIQUE KEY uk_category_difficulty (category_id, difficulty_id),
    INDEX idx_category_id (category_id),
    INDEX idx_difficulty_id (difficulty_id),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏类型-难度关联表';

-- 完整配置组合表 (用于快速查询有效的配置组合)
CREATE TABLE IF NOT EXISTS game_config_combinations (
    id VARCHAR(36) PRIMARY KEY,
    mode_id VARCHAR(36) NOT NULL,
    language_id VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    difficulty_id VARCHAR(36) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (mode_id) REFERENCES game_modes(id) ON DELETE CASCADE,
    FOREIGN KEY (language_id) REFERENCES game_languages(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES game_categories(id) ON DELETE CASCADE,
    FOREIGN KEY (difficulty_id) REFERENCES game_difficulties(id) ON DELETE CASCADE,
    UNIQUE KEY uk_config_combination (mode_id, language_id, category_id, difficulty_id),
    INDEX idx_mode_id (mode_id),
    INDEX idx_language_id (language_id),
    INDEX idx_category_id (category_id),
    INDEX idx_difficulty_id (difficulty_id),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏配置组合表'; 
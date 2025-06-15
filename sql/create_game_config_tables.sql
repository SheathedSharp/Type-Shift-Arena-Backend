-- 游戏配置管理系统数据库表
USE myappdb;

-- 1. 游戏模式表 (顶层)
CREATE TABLE IF NOT EXISTS game_modes (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '模式名称：如RANKED(排位赛), CASUAL(休闲赛), TUTORIAL(教程), CHALLENGE(挑战)',
    display_name VARCHAR(100) NOT NULL COMMENT '显示名称：如排位赛, 休闲赛, 教程模式, 挑战模式',
    description TEXT COMMENT '模式描述',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否激活',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_is_active (is_active),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏模式配置表';

-- 2. 游戏语言表
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

-- 3. 游戏类型表
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

-- 4. 游戏难度表 (底层)
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

-- 5. 游戏模式-语言关联表
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

-- 6. 游戏语言-类型关联表
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

-- 7. 游戏类型-难度关联表
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

-- 8. 完整配置组合表 (用于快速查询有效的配置组合)
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
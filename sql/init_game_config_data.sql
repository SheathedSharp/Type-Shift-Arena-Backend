-- 初始化游戏配置数据
USE myappdb;

-- 1. 初始化游戏模式数据
INSERT INTO game_modes (id, name, display_name, description, min_players, max_players, default_max_players, player_options, duration, is_active, sort_order) VALUES
(UUID(), 'CLASSIC', '经典模式', '传统的打字对战游戏，比拼速度与准确率', 2, 2, 2, '[2]', 3, TRUE, 1),
(UUID(), 'BUBBLE', '泡泡模式', '打字击破泡泡的创新游戏模式', 1, 8, 2, '[1,2,3,4,5,6,7,8]', 5, TRUE, 2),
(UUID(), 'IDIOMGUESS', '猜谜模式', '通过打字猜成语、词语的智力游戏模式', 2, 8, 2, '[2,4,6,8]', 3, TRUE, 3);

-- 2. 初始化游戏语言数据
INSERT INTO game_languages (id, code, name, display_name, is_active, sort_order) VALUES
(UUID(), 'zh', 'CHINESE', '简体中文', TRUE, 1),
(UUID(), 'en', 'ENGLISH', 'English', TRUE, 2),
(UUID(), 'ja', 'JAPANESE', '日本語', TRUE, 3),
(UUID(), 'ko', 'KOREAN', '한국어', TRUE, 4);

-- 3. 初始化游戏类型数据 
INSERT INTO game_categories (id, name, display_name, description, is_active, sort_order) VALUES
(UUID(), 'DAILY_CHAT', '日常聊天', '日常对话内容，适合练习日常交流', TRUE, 1),
(UUID(), 'ACADEMIC_WRITING', '学术写作', '学术论文和报告内容，适合学术场景练习', TRUE, 2),
(UUID(), 'LATEX_MATH', 'LaTeX数学', '数学公式和LaTeX代码，适合理工科学习', TRUE, 3),
(UUID(), 'PROGRAMMING', '编程解题', '编程代码和算法题目，适合程序员练习', TRUE, 4),
(UUID(), 'LITERATURE', '文学欣赏', '文学作品和经典文章，提升文学素养', TRUE, 5),
(UUID(), 'BUSINESS', '商务写作', '商业文档和邮件内容，适合职场练习', TRUE, 6);

-- 4. 初始化游戏难度数据 
INSERT INTO game_difficulties (id, name, display_name, description, level_value, is_active, sort_order) VALUES
(UUID(), 'EASY', '简单', '简单难度，适合初学者和练习基础技能', 1, TRUE, 1),
(UUID(), 'MEDIUM', '中等', '中等难度，适合有一定基础的玩家', 2, TRUE, 2),
(UUID(), 'HARD', '困难', '困难难度，适合高级玩家挑战', 3, TRUE, 3),
(UUID(), 'EXPERT', '专家', '专家难度，适合顶级玩家', 4, TRUE, 4),
(UUID(), 'MASTER', '大师', '大师难度，极限挑战', 5, TRUE, 5);

-- 5. 建立模式-语言关联 (所有模式支持所有语言)
INSERT INTO game_mode_languages (id, mode_id, language_id, is_active)
SELECT 
    UUID() as id,
    gm.id as mode_id, 
    gl.id as language_id,
    TRUE as is_active
FROM game_modes gm 
CROSS JOIN game_languages gl
WHERE gm.is_active = TRUE AND gl.is_active = TRUE;

-- 6. 建立语言-类型关联
-- 中文支持所有类型（因为新的游戏模式主要针对中文用户）
INSERT INTO game_language_categories (id, language_id, category_id, is_active)
SELECT 
    UUID() as id,
    gl.id as language_id,
    gc.id as category_id,
    TRUE as is_active
FROM game_languages gl
CROSS JOIN game_categories gc
WHERE gl.code = 'zh' 
  AND gl.is_active = TRUE AND gc.is_active = TRUE;

-- 英文支持所有类型
INSERT INTO game_language_categories (id, language_id, category_id, is_active)
SELECT 
    UUID() as id,
    gl.id as language_id,
    gc.id as category_id,
    TRUE as is_active
FROM game_languages gl
CROSS JOIN game_categories gc
WHERE gl.code = 'en' 
  AND gl.is_active = TRUE AND gc.is_active = TRUE;

-- 日文支持日常聊天、文学欣赏
INSERT INTO game_language_categories (id, language_id, category_id, is_active)
SELECT 
    UUID() as id,
    gl.id as language_id,
    gc.id as category_id,
    TRUE as is_active
FROM game_languages gl
CROSS JOIN game_categories gc
WHERE gl.code = 'ja' 
  AND gc.name IN ('DAILY_CHAT', 'LITERATURE')
  AND gl.is_active = TRUE AND gc.is_active = TRUE;

-- 韩文支持日常聊天、文学欣赏
INSERT INTO game_language_categories (id, language_id, category_id, is_active)
SELECT 
    UUID() as id,
    gl.id as language_id,
    gc.id as category_id,
    TRUE as is_active
FROM game_languages gl
CROSS JOIN game_categories gc
WHERE gl.code = 'ko' 
  AND gc.name IN ('DAILY_CHAT', 'LITERATURE')
  AND gl.is_active = TRUE AND gc.is_active = TRUE;

-- 7. 建立类型-难度关联 (所有类型支持基础三个难度)
INSERT INTO game_category_difficulties (id, category_id, difficulty_id, is_active)
SELECT 
    UUID() as id,
    gc.id as category_id,
    gd.id as difficulty_id,
    TRUE as is_active
FROM game_categories gc
CROSS JOIN game_difficulties gd
WHERE gc.is_active = TRUE 
  AND gd.is_active = TRUE
  AND gd.name IN ('EASY', 'MEDIUM', 'HARD');

-- 编程和LaTeX数学额外支持专家和大师难度
INSERT INTO game_category_difficulties (id, category_id, difficulty_id, is_active)
SELECT 
    UUID() as id,
    gc.id as category_id,
    gd.id as difficulty_id,
    TRUE as is_active
FROM game_categories gc
CROSS JOIN game_difficulties gd
WHERE gc.name IN ('PROGRAMMING', 'LATEX_MATH')
  AND gd.name IN ('EXPERT', 'MASTER')
  AND gc.is_active = TRUE AND gd.is_active = TRUE;

-- 8. 生成有效的配置组合 (通过关联表动态生成)
INSERT INTO game_config_combinations (id, mode_id, language_id, category_id, difficulty_id, is_active)
SELECT DISTINCT
    UUID() as id,
    gm.id as mode_id,
    gl.id as language_id,
    gc.id as category_id,
    gd.id as difficulty_id,
    TRUE as is_active
FROM game_modes gm
INNER JOIN game_mode_languages gml ON gm.id = gml.mode_id
INNER JOIN game_languages gl ON gml.language_id = gl.id
INNER JOIN game_language_categories glc ON gl.id = glc.language_id
INNER JOIN game_categories gc ON glc.category_id = gc.id
INNER JOIN game_category_difficulties gcd ON gc.id = gcd.category_id
INNER JOIN game_difficulties gd ON gcd.difficulty_id = gd.id
WHERE gm.is_active = TRUE 
  AND gl.is_active = TRUE 
  AND gc.is_active = TRUE 
  AND gd.is_active = TRUE
  AND gml.is_active = TRUE
  AND glc.is_active = TRUE
  AND gcd.is_active = TRUE; 
-- 初始化游戏配置数据
USE myappdb;

-- 1. 初始化游戏模式数据
INSERT INTO game_modes (id, name, display_name, description, is_active, sort_order) VALUES
(UUID(), 'RANKED', '排位赛', '竞技排位模式，影响玩家等级和排位积分', TRUE, 1),
(UUID(), 'CASUAL', '休闲赛', '休闲对战模式，不影响排位积分', TRUE, 2),
(UUID(), 'CUSTOM', '自定义', '自定义房间模式，可自由设置游戏参数', TRUE, 3),
(UUID(), 'TUTORIAL', '教程模式', '新手教学模式，帮助玩家熟悉游戏操作', TRUE, 4),
(UUID(), 'CHALLENGE', '挑战模式', '特殊挑战模式，提供各种有趣的挑战', TRUE, 5);

-- 2. 初始化游戏语言数据 (基于当前的TextLanguage枚举)
INSERT INTO game_languages (id, code, name, display_name, is_active, sort_order) VALUES
(UUID(), 'zh', 'CHINESE', '简体中文', TRUE, 1),
(UUID(), 'en', 'ENGLISH', 'English', TRUE, 2),
(UUID(), 'ja', 'JAPANESE', '日本語', TRUE, 3),
(UUID(), 'ko', 'KOREAN', '한국어', TRUE, 4);

-- 3. 初始化游戏类型数据 (基于当前的TextCategory枚举)
INSERT INTO game_categories (id, name, display_name, description, is_active, sort_order) VALUES
(UUID(), 'DAILY_CHAT', '日常聊天', '日常对话内容，适合练习日常交流', TRUE, 1),
(UUID(), 'ACADEMIC_WRITING', '学术写作', '学术论文和报告内容，适合学术场景练习', TRUE, 2),
(UUID(), 'LATEX_MATH', 'LaTeX数学', '数学公式和LaTeX代码，适合理工科学习', TRUE, 3),
(UUID(), 'PROGRAMMING', '编程解题', '编程代码和算法题目，适合程序员练习', TRUE, 4),
(UUID(), 'LITERATURE', '文学欣赏', '文学作品和经典文章，提升文学素养', TRUE, 5),
(UUID(), 'BUSINESS', '商务写作', '商业文档和邮件内容，适合职场练习', TRUE, 6);

-- 4. 初始化游戏难度数据 (基于当前的难度设置)
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
-- 中文支持文学欣赏、日常聊天、学术写作、商务写作
INSERT INTO game_language_categories (id, language_id, category_id, is_active)
SELECT 
    UUID() as id,
    gl.id as language_id,
    gc.id as category_id,
    TRUE as is_active
FROM game_languages gl
CROSS JOIN game_categories gc
WHERE gl.code = 'zh' 
  AND gc.name IN ('LITERATURE', 'DAILY_CHAT', 'ACADEMIC_WRITING', 'BUSINESS')
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
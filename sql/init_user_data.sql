-- 初始化用户数据
USE myappdb;

INSERT INTO users (id, username, password, email, imgSrc, role) VALUES
(UUID(), 'admin', '$2a$10$1b8gHcMeZud/P5UVizxA4.KnZ4m5yIovK1chBfQOaqPSNYFPw48Q2', 'admin@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin', 'admin');

CREATE DATABASE IF NOT EXISTS myappdb;

USE myappdb;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    imgSrc VARCHAR(255) DEFAULT 'https://api.dicebear.com/7.x/avataaars/svg?seed=',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

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

CREATE TABLE IF NOT EXISTS user_friends (
    user_id BIGINT,
    friend_id BIGINT,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (friend_id) REFERENCES users(id)
);
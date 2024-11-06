/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-27 15:39:23
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-06 09:03:47
 */
package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "imgSrc")  
    private String imgSrc = "https://api.dicebear.com/7.x/avataaars/svg?seed=";  // 使用 DiceBear API 生成默认头像

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // 设置随机默认头像
        if (imgSrc.equals("https://api.dicebear.com/7.x/avataaars/svg?seed=")) {
            imgSrc += username;  // 使用用户名作为种子生成唯一头像
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
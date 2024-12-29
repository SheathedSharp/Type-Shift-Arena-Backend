/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-27 15:39:23
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-10 12:26:24
 */
package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "users")
@Getter
@Setter
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PlayerProfile playerProfile;

    @ManyToMany
    @JoinTable(
        name = "user_friends",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @JsonIgnoreProperties("friends") // 忽略 friends 属性，避免循环引用
    private Set<User> friends = new HashSet<>();

    public User() {}

    public User(Long id, String username, String password, String email, String imgSrc) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.imgSrc = imgSrc;
    }

    public User setUsername(String username) {
        this.username = username;
        return this; // Return the current instance
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
/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-11-07 08:48:34
 */
package com.example.demo.model.dto;

import com.example.demo.entity.User;

public class FriendDTO {
    private String id;
    private String username;
    private String email;
    private String imgSrc;
    
    // 无参构造函数
    public FriendDTO() {
    }
    
    // 带参构造函数
    public FriendDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.imgSrc = user.getImgSrc();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }   
} 
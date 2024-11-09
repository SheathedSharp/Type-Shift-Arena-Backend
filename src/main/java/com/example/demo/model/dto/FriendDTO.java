/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-07 08:48:34
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-07 08:56:41
 */
package com.example.demo.dto;

import com.example.demo.model.User;

public class FriendDTO {
    private Long id;
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
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
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
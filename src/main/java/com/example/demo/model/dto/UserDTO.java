/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-13 18:07:49
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-14 22:20:28
 */
package com.example.demo.model.dto;

import com.example.demo.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String imgSrc;
    
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.imgSrc = user.getImgSrc();
    }
}

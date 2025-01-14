/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-11-13 18:07:49
 */
package com.example.demo.model.dto;

import com.example.demo.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String id;
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

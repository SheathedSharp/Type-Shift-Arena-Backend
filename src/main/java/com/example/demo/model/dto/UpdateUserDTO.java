package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private String username;
    private String email;
    private String imgSrc;

    public UpdateUserDTO(String username, String email, String imgSrc) {
        this.username = username;
        this.email = email;
        this.imgSrc = imgSrc;
    }


}

package com.example.demo.model.dto;

 import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {
    private String username;
    private String password;
    private String email;
}


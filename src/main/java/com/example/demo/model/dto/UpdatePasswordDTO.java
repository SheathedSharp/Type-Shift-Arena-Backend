package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {
    private String oldPassword; // 旧密码，进行验证时可以用到
    private String newPassword; // 新密码
    private String confirmPassword; // 确认密码，进行验证时可以用到
}


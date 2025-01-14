/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-10-27 18:20:34
 */
package com.example.demo.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.core.ApiResponse;
import com.example.demo.entity.User;
import com.example.demo.model.dto.UpdatePasswordDTO;
import com.example.demo.model.dto.UpdateUserDTO;
import com.example.demo.model.dto.UserCreateDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get a user by ID", description = "Returns a user as per the id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
            @Parameter(description = "ID of user to be searched") @PathVariable String id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(ApiResponse.success("User found successfully", new UserDTO(user))))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a user", description = "Creates a new user with the provided details")
    @PostMapping

    public ResponseEntity<?> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        // 验证用户名是否已存在
        if (userService.existsByUsername(userCreateDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // 验证邮箱是否已存在
        if (userService.existsByEmail(userCreateDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        // 创建新用户
        User newUser = new User();
        newUser.setUsername(userCreateDTO.getUsername());
        newUser.setPassword(userCreateDTO.getPassword());
        newUser.setEmail(userCreateDTO.getEmail());

        // 调用服务层方法保存用户
        userService.saveUser(newUser);

        // 返回创建成功的响应
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UpdateUserDTO updateUserDTO) {
        User updatedUser = userService.updateUserById(id, updateUserDTO);

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Update a user's password", description = "Updates a user's password ")
    @PutMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable String id, @RequestBody UpdatePasswordDTO updatePasswordDTO)
    {
        // 调用服务层方法，尝试更新密码
        userService.updatePassword(id, updatePasswordDTO);
        return ResponseEntity.ok("Password updated successfully");
    }


    @Operation(summary = "Delete a user", description = "Deletes a user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        return userService.getUserById(id)
                .map(user -> {
                    userService.deleteUser(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Returns a user as per the username")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByUsername(
            @Parameter(description = "Username") @PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(user -> ResponseEntity.ok(ApiResponse.success("User found successfully", new UserDTO(user))))
                .orElse(ResponseEntity.notFound().build());
    }
}

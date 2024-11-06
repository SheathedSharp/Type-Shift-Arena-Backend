/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-27 15:39:23
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-06 11:26:32
 */
package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.PlayerProfile;
import com.example.demo.repository.PlayerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PlayerProfileRepository playerProfileRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 保存用户
        User savedUser = userRepository.save(user);

        // 创建并初始化玩家资料
        PlayerProfile profile = new PlayerProfile();
        profile.setUser(savedUser);
        playerProfileRepository.save(profile);

        return savedUser;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

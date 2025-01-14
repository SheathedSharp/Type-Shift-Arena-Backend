/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-10-27 15:39:23
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2025-01-03 17:17:13
 */
package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findByUsernameContaining(String username);
}

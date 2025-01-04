/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-06 08:43:26
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2025-01-03 17:23:10
 */
package com.example.demo.repository;

import com.example.demo.entity.PlayerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerProfileRepository extends JpaRepository<PlayerProfile, String> {
}
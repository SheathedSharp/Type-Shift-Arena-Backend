/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-06 08:43:26
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-10 10:40:51
 */
package com.example.demo.repository;

import com.example.demo.entity.PlayerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerProfileRepository extends JpaRepository<PlayerProfile, Long> {
}
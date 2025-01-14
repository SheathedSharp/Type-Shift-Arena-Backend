/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-30 18:56:21
 */
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.GameMatch;

@Repository
public interface GameMatchRepository extends JpaRepository<GameMatch, String> { // 改为String类型
    List<GameMatch> findByPlayer1IdOrPlayer2Id(String player1Id, String player2Id);
    List<GameMatch> findByWinnerId(String winnerId);
}
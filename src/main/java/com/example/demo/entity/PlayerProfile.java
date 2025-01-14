/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-11-10 10:17:27
 */
package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "player_profile")
@Getter
@Setter
public class PlayerProfile {
    @Id
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore // 避免返回User对象时出现循环引用
    private User user;

    @Column(name = "user_level") 
    private String userLevel = "无等级";    // 用户等级（段位）

    // === 排位赛数据 ===
    @Column(name = "ranked_matches_played")
    private int rankedMatchesPlayed = 0;  // 排位赛场数

    @Column(name = "ranked_wins")
    private int rankedWins = 0;  // 排位赛胜场

    @Column(name = "ranked_win_rate")
    private double rankedWinRate = 0.0;  // 排位赛胜率

    @Column(name = "ranked_avg_wpm")
    private double rankedAvgWpm = 0.0;  // 排位赛平均速度

    @Column(name = "ranked_avg_accuracy")
    private double rankedAvgAccuracy = 0.0;  // 排位赛平均准确率

    @Column(name = "rank_score")
    private int rankScore = 500;  // 段位积分

    // === 非排位赛数据 ===
    @Column(name = "casual_matches_played")
    private int casualMatchesPlayed = 0;  // 非排位赛场数

    @Column(name = "casual_wins")
    private int casualWins = 0;  // 非排位赛胜场

    @Column(name = "casual_win_rate")
    private double casualWinRate = 0.0;  // 非排位赛胜率

    @Column(name = "casual_avg_wpm")
    private double casualAvgWpm = 0.0;  // 非排位赛平均速度

    @Column(name = "casual_avg_accuracy")
    private double casualAvgAccuracy = 0.0;  // 非排位赛平均准确率

    // === 总体数据 ===
    @Column(name = "total_matches_played")
    private int totalMatchesPlayed = 0;  // 总场数

    @Column(name = "total_wins")
    private int totalWins = 0;  // 总胜场

    @Column(name = "highest_wpm")
    private double highestWpm = 0.0; // 最高速度

    @Column(name = "total_win_rate")
    private double totalWinRate = 0.0;  // 总胜率

    @Column(name = "total_avg_wpm")
    private double totalAvgWpm = 0.0;  // 总平均速度

    @Column(name = "total_avg_accuracy")
    private double totalAvgAccuracy = 0.0;  // 总平均准确率
}




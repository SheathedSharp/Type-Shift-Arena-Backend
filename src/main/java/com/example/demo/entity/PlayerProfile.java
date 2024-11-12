/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-10 10:17:27
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-10 11:12:07
 */
package com.example.demo.entity;

import com.example.demo.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "player_profile")
@Getter
@Setter
public class PlayerProfile {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "userLevel") 
    private String userLevel = "无等级";    // 用户等级（段位）

    @Column(name = "win") 
    private int win;  // 胜利场数

    @Column(name = "speed")      
    private double speed;        // 均速

    @Column(name = "winRate") 
    private double winRate;      // 胜率

    @Column(name = "accuracyRate")
    private double accuracyRate;  // 准确率

    @Column(name = "rankScore")
    private int rankScore = 500; // 初始设置为500分

    @Column(name = "matchesPlayed")
    private int matchesPlayed = 0; // 总比赛场数
}




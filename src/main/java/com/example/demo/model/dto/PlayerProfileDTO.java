/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-14 22:00:32
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-14 22:02:50
 */
package com.example.demo.model.dto;

import com.example.demo.entity.PlayerProfile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerProfileDTO {
    private Long id;
    private String username;
    private String userLevel;
    private int win;
    private double speed;
    private double winRate;
    private double accuracyRate;
    private int rankScore;
    private int matchesPlayed;

    public PlayerProfileDTO(PlayerProfile profile) {
        this.id = profile.getId();
        this.username = profile.getUser().getUsername();
        this.userLevel = profile.getUserLevel();
        this.win = profile.getWin();
        this.speed = profile.getSpeed();
        this.winRate = profile.getWinRate();
        this.accuracyRate = profile.getAccuracyRate();
        this.rankScore = profile.getRankScore();
        this.matchesPlayed = profile.getMatchesPlayed();
    }
} 
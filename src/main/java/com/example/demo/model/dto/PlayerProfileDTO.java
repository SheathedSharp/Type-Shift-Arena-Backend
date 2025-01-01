/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-14 22:00:32
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2024-12-30 19:44:07
 */
package com.example.demo.model.dto;

import com.example.demo.entity.PlayerProfile;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerProfileDTO {
    private Long id;
    private String username;
    private String userLevel;
    
    // === 排位赛数据 ===
    private int rankedMatchesPlayed;
    private int rankedWins;
    private double rankedWinRate;
    private double rankedAvgWpm;
    private double rankedAvgAccuracy;
    private int rankScore;
    
    // === 非排位赛数据 ===
    private int casualMatchesPlayed;
    private int casualWins;
    private double casualWinRate;
    private double casualAvgWpm;
    private double casualAvgAccuracy;
    
    // === 总体数据 ===
    private int totalMatchesPlayed;
    private int totalWins;
    private double highestWpm;
    private double totalWinRate;
    private double totalAvgWpm;
    private double totalAvgAccuracy;

    public PlayerProfileDTO(PlayerProfile profile) {
        if (profile != null) {
            this.id = profile.getId();
            this.username = profile.getUser() != null ? profile.getUser().getUsername() : null;
            this.userLevel = profile.getUserLevel();
            
            // 排位赛数据
            this.rankedMatchesPlayed = profile.getRankedMatchesPlayed();
            this.rankedWins = profile.getRankedWins();
            this.rankedWinRate = profile.getRankedWinRate();
            this.rankedAvgWpm = profile.getRankedAvgWpm();
            this.rankedAvgAccuracy = profile.getRankedAvgAccuracy();
            this.rankScore = profile.getRankScore();
            
            // 非排位赛数据
            this.casualMatchesPlayed = profile.getCasualMatchesPlayed();
            this.casualWins = profile.getCasualWins();
            this.casualWinRate = profile.getCasualWinRate();
            this.casualAvgWpm = profile.getCasualAvgWpm();
            this.casualAvgAccuracy = profile.getCasualAvgAccuracy();
            
            // 总体数据
            this.totalMatchesPlayed = profile.getTotalMatchesPlayed();
            this.totalWins = profile.getTotalWins();
            this.highestWpm = profile.getHighestWpm();
            this.totalWinRate = profile.getTotalWinRate();
            this.totalAvgWpm = profile.getTotalAvgWpm();
            this.totalAvgAccuracy = profile.getTotalAvgAccuracy();
        }
    }
} 
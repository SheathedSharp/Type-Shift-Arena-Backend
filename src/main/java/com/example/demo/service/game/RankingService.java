/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-30 20:56:12
 */

package com.example.demo.service.game;

import org.springframework.stereotype.Service;

import com.example.demo.entity.PlayerProfile;

@Service
public class RankingService {
    // 基础分数配置
    private static final int BASE_WIN_SCORE = 100;
    private static final int BASE_LOSS_SCORE = -50;
    
    // 计算排位积分变化
    public int calculateRankScoreChange(
            PlayerProfile profile,
            double currentWpm,
            double currentAccuracy,
            boolean isVictory) {
        
        // 基础分数
        int baseScore = isVictory ? BASE_WIN_SCORE : BASE_LOSS_SCORE;
        
        // 表现加成系数 (0.8 - 1.2)
        double performanceMultiplier = calculatePerformanceMultiplier(
            profile, currentWpm, currentAccuracy);
            
        // 积分段位系数 (0.8 - 1.2)
        double rankMultiplier = calculateRankMultiplier(profile.getRankScore());
        
        // 计算最终分数变化
        return (int) Math.round(baseScore * performanceMultiplier * rankMultiplier);
    }
    
    private double calculatePerformanceMultiplier(
            PlayerProfile profile,
            double currentWpm,
            double currentAccuracy) {
        
        // 计算WPM表现 (-0.2 到 0.2)
        double wpmFactor = 0;
        if (profile.getRankedMatchesPlayed() > 0) {
            double wpmDiff = currentWpm - profile.getRankedAvgWpm();
            wpmFactor = Math.min(Math.max(wpmDiff / profile.getRankedAvgWpm(), -0.2), 0.2);
        }
        
        // 计算准确率表现 (-0.1 到 0.1)
        double accuracyFactor = 0;
        if (profile.getRankedMatchesPlayed() > 0) {
            double accDiff = currentAccuracy - profile.getRankedAvgAccuracy();
            accuracyFactor = Math.min(Math.max(accDiff / 100, -0.1), 0.1);
        }
        
        // 基础系数1.0加上表现因子
        return 1.0 + wpmFactor + accuracyFactor;
    }
    
    private double calculateRankMultiplier(int currentRankScore) {
        // 段位系数：分数越高，输赢的分数变化越小
        if (currentRankScore >= 4000) return 0.8;  // 最强王者/超凡大师
        if (currentRankScore >= 3500) return 0.9;  // 璀璨钻石
        if (currentRankScore >= 3000) return 1.0;  // 耀眼翡翠
        if (currentRankScore >= 2500) return 1.1;  // 华贵铂金
        return 1.2;  // 低段位
    }
}
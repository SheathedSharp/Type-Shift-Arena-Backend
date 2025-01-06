/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-06 10:53:19
 */
package com.example.demo.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.PlayerProfile;
import com.example.demo.repository.PlayerProfileRepository;
import com.example.demo.service.game.RankingService;


@Service
public class PlayerProfileService {
    @Autowired
    private PlayerProfileRepository playerProfileRepository;
    @Autowired
    private RankingService rankingService;
    
    // 更新玩家数据
    public PlayerProfile updatePlayerStats(String id, double wpm, double accuracy, 
                                         boolean isVictory, boolean isRanked) {
        PlayerProfile profile = getPlayerProfileById(id);
        if (profile == null) {
            throw new RuntimeException("Player profile not found");
        }
        
        // 更新总体数据
        profile.setTotalMatchesPlayed(profile.getTotalMatchesPlayed() + 1);
        if (isVictory) {
            profile.setTotalWins(profile.getTotalWins() + 1);
        }
        
        // 更新最高记录
        if (wpm > profile.getHighestWpm()) {
            profile.setHighestWpm(wpm);
        }
        
        if (isRanked) {
            updateRankedStats(profile, wpm, accuracy, isVictory);
        } else {
            updateCasualStats(profile, wpm, accuracy, isVictory);
        }
        
        return playerProfileRepository.save(profile);
    }

    private void updateRankedStats(PlayerProfile profile, double wpm, 
                                 double accuracy, boolean isVictory) {
        // 更新排位赛场次
        profile.setRankedMatchesPlayed(profile.getRankedMatchesPlayed() + 1);
        
        // 更新排位赛胜场
        if (isVictory) {
            profile.setRankedWins(profile.getRankedWins() + 1);
        }
        
        // 更新排位赛平均速度
        double newWpm = ((profile.getRankedAvgWpm() * 
            (profile.getRankedMatchesPlayed() - 1)) + wpm) 
            / profile.getRankedMatchesPlayed();
        profile.setRankedAvgWpm(newWpm);
        
        // 更新排位赛胜率
        double newWinRate = (double) profile.getRankedWins() / 
            profile.getRankedMatchesPlayed() * 100;
        profile.setRankedWinRate(newWinRate);
        
        // 更新排位赛准确率
        double newAccuracy = ((profile.getRankedAvgAccuracy() * 
            (profile.getRankedMatchesPlayed() - 1)) + accuracy) 
            / profile.getRankedMatchesPlayed();
        profile.setRankedAvgAccuracy(newAccuracy);
        
        // 更新段位积分
        int scoreChange = rankingService.calculateRankScoreChange(
            profile, wpm, accuracy, isVictory);
        profile.setRankScore(Math.max(0, profile.getRankScore() + scoreChange));
        
        // 更新等级
        updateUserLevel(profile);
    }

    private void updateCasualStats(PlayerProfile profile, double wpm, 
                                 double accuracy, boolean isVictory) {
        // 更新非排位赛场次
        profile.setCasualMatchesPlayed(profile.getCasualMatchesPlayed() + 1);
        
        // 更新非排位赛胜场
        if (isVictory) {
            profile.setCasualWins(profile.getCasualWins() + 1);
        }
        
        // 更新非排位赛平均速度
        double newWpm = ((profile.getCasualAvgWpm() * 
            (profile.getCasualMatchesPlayed() - 1)) + wpm) 
            / profile.getCasualMatchesPlayed();
        profile.setCasualAvgWpm(newWpm);
        
        // 更新非排位赛胜率
        double newWinRate = (double) profile.getCasualWins() / 
            profile.getCasualMatchesPlayed() * 100;
        profile.setCasualWinRate(newWinRate);
        
        // 更新非排位赛准确率
        double newAccuracy = ((profile.getCasualAvgAccuracy() * 
            (profile.getCasualMatchesPlayed() - 1)) + accuracy) 
            / profile.getCasualMatchesPlayed();
        profile.setCasualAvgAccuracy(newAccuracy);
    }

    private void updateUserLevel(PlayerProfile profile) {
        String newLevel;
        if (profile.getRankScore() < 1500) newLevel = "倔强青铜";
        else if (profile.getRankScore() < 2000) newLevel = "不屈白银";
        else if (profile.getRankScore() < 2500) newLevel = "荣耀黄金";
        else if (profile.getRankScore() < 3000) newLevel = "华贵铂金";
        else if (profile.getRankScore() < 3500) newLevel = "耀眼翡翠"; 
        else if (profile.getRankScore() < 4000) newLevel = "璀璨钻石";
        else if (profile.getRankScore() < 4500) newLevel = "超凡大师";
        else newLevel = "最强王者";
        
        profile.setUserLevel(newLevel);
    }
    
    public PlayerProfile getPlayerProfileById(String id) {
        return playerProfileRepository.findById(id).orElse(null);
    }
}
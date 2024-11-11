/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-06 10:53:19
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-10 11:13:52
 */
package com.example.demo.service.user;

import com.example.demo.entity.PlayerProfile;
import com.example.demo.repository.PlayerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class PlayerProfileService {
    @Autowired
    private PlayerProfileRepository playerProfileRepository;
    
    public PlayerProfile updatePlayerStats(Long id, double speed, boolean isVictory, double accuracy) {
        PlayerProfile profile = getPlayerProfileById(id);
        if (profile == null) {
            throw new RuntimeException("Player profile not found");
        }
        
        // 更新比赛场次
        profile.setMatchesPlayed(profile.getMatchesPlayed() + 1);
        
        // 更新胜场
        if (isVictory) {
            profile.setWin(profile.getWin() + 1);
        }
        
        // 更新平均速度
        double newSpeed = ((profile.getSpeed() * (profile.getMatchesPlayed() - 1)) + speed) 
            / profile.getMatchesPlayed();
        profile.setSpeed(newSpeed);
        
        // 更新胜率
        double newWinRate = (double) profile.getWin() / profile.getMatchesPlayed() * 100;
        profile.setWinRate(newWinRate);
        
        // 更新准确率
        double newAccuracyRate = ((profile.getAccuracyRate() * (profile.getMatchesPlayed() - 1)) + accuracy) 
            / profile.getMatchesPlayed();
        profile.setAccuracyRate(newAccuracyRate);
        
        // 更新段位积分
        int newRankScore = profile.getRankScore() + (isVictory ? 100 : -50);
        profile.setRankScore(newRankScore);
        
        // 更新等级
        updateUserLevel(profile);
        
        return playerProfileRepository.save(profile);
    }
    
    private void updateUserLevel(PlayerProfile profile) {
        String newLevel;
        if (profile.getRankScore() < 1000) newLevel = "倔强青铜";
        else if (profile.getRankScore() < 1500) newLevel = "不屈白银";
        else if (profile.getRankScore() < 2000) newLevel = "荣耀黄金";
        else if (profile.getRankScore() < 2500) newLevel = "华贵铂金";
        else if (profile.getRankScore() < 3000) newLevel = "耀眼翡翠"; 
        else if (profile.getRankScore() < 3500) newLevel = "璀璨钻石";
        else if (profile.getRankScore() < 4000) newLevel = "超凡大师";
        else newLevel = "最强王者";
        
        profile.setUserLevel(newLevel);
    }
    
    public PlayerProfile getPlayerProfileById(Long id) {
        return playerProfileRepository.findById(id).orElse(null);
    }
}
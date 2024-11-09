/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-06 10:53:19
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-09 18:07:01
 */
package com.example.demo.service.user;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.model.PlayerProfile;
import com.example.demo.repository.PlayerProfileRepository;

@Service
public class PlayerProfileService {
    @Autowired
    private PlayerProfileRepository playerProfileRepository;
    
    public PlayerProfile getPlayerProfileById(Long id) {
        return playerProfileRepository.findById(id).orElse(null);
    }
    
    public PlayerProfile updatePlayerStats(Long id, double speed, boolean isVictory, double accuracy) {
        PlayerProfile profile = getPlayerProfileById(id);
        profile.updateStats(speed, isVictory, accuracy);
        return playerProfileRepository.save(profile);
    }  
    
    public String getUserLevel(Long id) {
        PlayerProfile profile = getPlayerProfileById(id);
        return profile.getUserLevel();
    }

}
/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-06 10:52:43
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-11 11:00:03
 */
package com.example.demo.controller.user;

import com.example.demo.entity.PlayerProfile;
import com.example.demo.service.user.PlayerProfileService; 
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/players")
public class PlayerProfileController {
    @Autowired
    private PlayerProfileService playerProfileService;
    
    @Operation(summary = "Get a player profile by ID", description = "Returns a player profile as per the id")
    @GetMapping("/{id}")
    public PlayerProfile getPlayerProfile(@PathVariable Long id) {
        return playerProfileService.getPlayerProfileById(id);
    }

    @Operation(summary = "Update player stats", description = "Updates player stats with the provided details")
    @PutMapping("/{id}/stats")
    public PlayerProfile updatePlayerStats(
        @PathVariable Long id,
        @RequestParam double speed,
        @RequestParam boolean isVictory,
        @RequestParam double accuracy) {
        return playerProfileService.updatePlayerStats(id, speed, isVictory, accuracy);
    }

    // @Operation(summary = "Get user level", description = "Returns the user level as per the id")
    // @GetMapping("/{id}/level")
    // public String getUserLevel(@PathVariable Long id) {
    //     return playerProfileService.getUserLevel(id);
    // }
}
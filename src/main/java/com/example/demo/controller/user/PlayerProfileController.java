/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-06 10:52:43
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-14 22:25:58
 */
package com.example.demo.controller.user;

import com.example.demo.entity.PlayerProfile;
import com.example.demo.service.user.PlayerProfileService; 
import com.example.demo.core.ApiResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.Operation;
import com.example.demo.model.dto.PlayerProfileDTO;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/players")
public class PlayerProfileController {
    @Autowired
    private PlayerProfileService playerProfileService;
    
    @Operation(summary = "Get a player profile by ID", description = "Returns a player profile as per the id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlayerProfileDTO>> getPlayerProfile(@PathVariable Long id) {
        PlayerProfile profile = playerProfileService.getPlayerProfileById(id);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success("Player profile found successfully", new PlayerProfileDTO(profile)));
    }

    @Operation(summary = "Update player stats", description = "Updates player stats with the provided details")
    @PutMapping("/{id}/stats")
    public ResponseEntity<ApiResponse<PlayerProfileDTO>> updatePlayerStats(
        @PathVariable Long id,
        @RequestParam double speed,
        @RequestParam boolean isVictory,
        @RequestParam double accuracy) {
        PlayerProfile updatedProfile = playerProfileService.updatePlayerStats(id, speed, isVictory, accuracy);
        return ResponseEntity.ok(ApiResponse.success("Player stats updated successfully", new PlayerProfileDTO(updatedProfile)));
    }

    // @Operation(summary = "Get user level", description = "Returns the user level as per the id")
    // @GetMapping("/{id}/level")
    // public String getUserLevel(@PathVariable Long id) {
    //     return playerProfileService.getUserLevel(id);
    // }
}
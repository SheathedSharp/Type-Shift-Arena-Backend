/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-11-06 10:52:43
 */
package com.example.demo.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.core.ApiResponse;
import com.example.demo.entity.PlayerProfile;
import com.example.demo.model.dto.PlayerProfileDTO;
import com.example.demo.service.user.PlayerProfileService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/players")
public class PlayerProfileController {
    @Autowired
    private PlayerProfileService playerProfileService;
    
    @Operation(summary = "Get a player profile by ID", description = "Returns a player profile as per the id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlayerProfileDTO>> getPlayerProfile(@PathVariable String id) {
        PlayerProfile profile = playerProfileService.getPlayerProfileById(id);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success("Player profile found successfully", new PlayerProfileDTO(profile)));
    }
}
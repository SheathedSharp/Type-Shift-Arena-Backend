/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-12-31 09:55:16
 */
package com.example.demo.controller.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.core.ApiResponse;
import com.example.demo.entity.GameMatch;
import com.example.demo.service.game.GameMatchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/matches")
@Tag(name = "Game Matches", description = "APIs for retrieving game matches")
public class GameMatchController {
    
    @Autowired
    private GameMatchService gameMatchService;
    
    @Operation(summary = "Get player's match history", 
              description = "Returns all matches of a player")
    @GetMapping("/player/{playerId}")
    public ResponseEntity<ApiResponse<List<GameMatch>>> getPlayerMatches(
            @PathVariable String playerId,
            @RequestParam(required = false) Boolean ranked) {
        try {
            List<GameMatch> matches;
            if (ranked != null) {
                // 如果指定了ranked参数，返回对应类型的比赛
                matches = ranked ? 
                    gameMatchService.getPlayerRankedMatches(playerId) :
                    gameMatchService.getPlayerCasualMatches(playerId);
            } else {
                // 如果没有指定，返回所有比赛
                matches = gameMatchService.getPlayerMatches(playerId);
            }
            
            return ResponseEntity.ok(ApiResponse.success(
                "Player matches retrieved successfully", 
                matches
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error retrieving player matches: " + e.getMessage()));
        }
    }
}
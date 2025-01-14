/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-11-14 17:01:26
 * @LastEditors: SheathedSharp z404878860@163.com
 * @LastEditTime: 2024-11-14 22:36:18
 */
package com.example.demo.controller.game;

import com.example.demo.service.game.MatchmakingService;
import com.example.demo.model.game.MatchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MatchmakingController {
    private final MatchmakingService matchmakingService;
    private static final Logger logger = LoggerFactory.getLogger(MatchmakingController.class);

    @Autowired
    public MatchmakingController(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }

    @MessageMapping("/matchmaking/queue")
    public void joinQueue(MatchRequest request) {
        logger.info("Player {} joining matchmaking queue with language={}, category={}, difficulty={}",
            request.getPlayerId(), request.getLanguage(), request.getCategory(), request.getDifficulty());
        
        matchmakingService.addToQueue(request);
    }

    @MessageMapping("/matchmaking/cancel")
    public void cancelQueue(MatchRequest request) {
        logger.info("Player {} canceling matchmaking", request.getPlayerId());
        matchmakingService.removeFromQueue(request);
    }
} 
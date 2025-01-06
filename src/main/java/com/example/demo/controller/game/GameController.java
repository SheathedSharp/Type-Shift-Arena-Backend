/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-28 20:03:44
 */
package com.example.demo.controller.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.example.demo.model.dto.game.GameMatchDTO;
import com.example.demo.model.game.GameMessage;
import com.example.demo.model.game.GameProgress;
import com.example.demo.service.game.GameInviteService;
import com.example.demo.service.game.GameMatchService;
import com.example.demo.service.game.GameService;

@Controller
public class GameController {
    /*
        游戏控制器
        用于处理游戏相关的操作
    */
    
    private static final Logger logger = LoggerFactory.getLogger(GameController.class); // 日志记录器

    @Autowired
    private GameService gameService; // 游戏服务

    @Autowired
    private GameMatchService gameMatchService; // 比赛服务

    @Autowired
    private GameInviteService gameInviteService; // 游戏邀请服务

    /* 
        映射注解说明
        @MessageMapping: 用于处理WebSocket消息的映射，通常用于处理客户端发送的消息。@DestinationVariable: 用于从路径变量中提取参数。
    */

    // 开始游戏
    @MessageMapping("/room/{roomId}/start")
    public void startGame(@DestinationVariable String roomId, GameMessage message) {
        logger.info("Starting game in room {}", roomId);
        
        gameService.gameStart(roomId, message);
    }

    // 更新游戏进度
    @MessageMapping("/room/{roomId}/progress")
    public void updateProgress(@DestinationVariable String roomId, GameProgress progress) {
        logger.debug("Progress update in room {}: Player {} at {}%, WPM: {}, Accuracy: {}%", 
            roomId, progress.getPlayerId(), progress.getStats().getProgress(), 
            progress.getStats().getWpm(), progress.getStats().getAccuracy());
        
        gameService.updateProgress(roomId, progress);
    }

    // 结束游戏
    @MessageMapping("/room/{roomId}/finish")
    public void finishGame(@DestinationVariable String roomId, GameMessage message) {
        logger.info("Player {} finished in room {}", message.getPlayerId(), roomId);

        gameService.gameEnd(roomId, message);
    }

    // 记录比赛结果
    @MessageMapping("/room/{roomId}/record")
    public void recordMatch(@DestinationVariable String roomId, GameMatchDTO matchDTO) {
        logger.info("Recording match result for room {}", roomId);
        gameMatchService.recordMatch(matchDTO);
    }

    // 发送游戏邀请
    @MessageMapping("/game/invite")
    public void sendGameInvite(GameMessage message) {
        logger.info("Game invite sent from {} to {}", message.getPlayerId(), message.getOpponentId());
        
        gameInviteService.sendGameInvite(
            message.getPlayerId(),
            message.getOpponentId(),
            message.getRoomId()
        );
    }
}

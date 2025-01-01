package com.example.demo.service.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.entity.GameText;
import com.example.demo.model.game.GameMessage;
import com.example.demo.model.game.GameProgress;
import com.example.demo.model.game.GameRoom;
import com.example.demo.model.game.GameStatus;

@Service
public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private final GameRoomService gameRoomService;

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private final GameTextService gameTextService;

    public GameService(GameRoomService gameRoomService, SimpMessagingTemplate messagingTemplate, GameTextService gameTextService) {
        this.gameRoomService = gameRoomService;
        this.messagingTemplate = messagingTemplate;
        this.gameTextService = gameTextService;
    }

    public void gameStart(String roomId, GameMessage message) {
        GameRoom room = gameRoomService.getRoom(roomId);
        if (room != null) {
            // 验证是否为房主发起的请求
            if (!room.getHostId().equals(message.getPlayerId())) {
                logger.warn("Non-host player {} attempted to start game in room {}", 
                    message.getPlayerId(), roomId);
                return;
            }
            
            // 设置游戏状态为进行中
            setGameStatus(room, GameStatus.PLAYING);
            
            // 广播游戏开始消息
            messagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                new GameMessage() {{
                    setType("GAME_START");
                    setRoomStatus(GameStatus.PLAYING.toString());
                    setStartTime(room.getStartTime());
                    setTargetText(room.getTargetText());
                    setTimestamp(System.currentTimeMillis());
                }}
            );
        }
    }

    public void gameEnd(String roomId, GameMessage message) {
        GameRoom room = gameRoomService.getRoom(roomId);
        if (room != null) {
            room.setStatus(GameStatus.FINISHED);
            
            // 广播游戏结束消息
            messagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                new GameMessage() {{
                    setType("GAME_FINISH");
                    setRoomStatus(room.getStatus().toString());
                    setPlayerId(message.getPlayerId());
                    setTimestamp(message.getTimestamp());
                }}
            );
        }
    }

    public void updateProgress(String roomId, GameProgress progress) {
        GameRoom room = gameRoomService.getRoom(roomId);
        if (room != null) {
            progress.setType("PLAYER_PROGRESS");            // 确保消息类型为PLAYER_PROGRESS
            progress.setTimestamp(System.currentTimeMillis());  // 设置时间戳

            // 找到另一个玩家
            String otherPlayerId = room.getPlayersId().stream()
                .filter(id -> !id.equals(progress.getPlayerId()))
                .findFirst()
                .orElse(null);

            // 通知另一个玩家
            messagingTemplate.convertAndSend(
                "/queue/room/" + otherPlayerId + "/info",
                progress
            );
        }
    }

    public void updateGameStatus(String roomId, GameStatus status) {
        GameRoom room = gameRoomService.getRoom(roomId);
        if (room != null) {
            setGameStatus(room, status);
        }
    }

    public void setGameStatus(GameRoom room, GameStatus status) {
        room.setStatus(status);
        if (status == GameStatus.PLAYING) {
            // 获取并设置目标文本
            GameText gameText = gameTextService.getRandomText(
                room.getLanguage(), 
                room.getCategory(), 
                room.getDifficulty(), 
                false
            );
            room.setTargetText(gameText.getContent());
            room.setStartTime(System.currentTimeMillis());
        }
    }

}

/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-29 22:46:56
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-06 21:32:13
 */
package com.example.demo.controller.game;

import com.example.demo.model.game.GameRoom;
import com.example.demo.model.game.GameStatus;
import com.example.demo.model.game.GameMessage;
import com.example.demo.service.game.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation;

@Controller
public class GameRoomController {
    private static final Logger logger = LoggerFactory.getLogger(GameRoomController.class);

    @Autowired
    private GameRoomService gameRoomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/room/{roomId}/join")
    public void joinRoom(@DestinationVariable String roomId, GameMessage message) {
        logger.info("Player {} joining room {}", message.getPlayerId(), roomId);
        GameRoom room = gameRoomService.joinRoom(roomId, message.getPlayerId(), message.getPlayerName());
        
        // Broadcast the join event to all players in the room
        messagingTemplate.convertAndSend(
            "/topic/room/" + roomId,
            new GameMessage() {{
                setType("PLAYER_JOIN");
                setPlayerId(message.getPlayerId());
                setPlayerName(message.getPlayerName());
                setTimestamp(message.getTimestamp());
            }}
        );

        // Check if the room is full, if so, start the game automatically
        if (room.isFull()) {
            logger.info("Room {} is full, starting game automatically", roomId);
            room.setStatus(GameStatus.PLAYING);
            
            // Broadcast game start
            messagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                new GameMessage() {{
                    setType("GAME_START");
                    setTimestamp(new Date().toInstant().toString());
                }}
            );
        }
    }

    @MessageMapping("/room/{roomId}/leave")
    public void leaveRoom(@DestinationVariable String roomId, GameMessage message) {
        logger.info("Player {} leaving room {}", message.getPlayerId(), roomId);
        gameRoomService.leaveRoom(roomId, message.getPlayerId(), message.getPlayerName());
        
        // Broadcast the leave event
        messagingTemplate.convertAndSend(
            "/topic/room/" + roomId,
            new GameMessage() {{
                setType("PLAYER_LEAVE");
                setPlayerId(message.getPlayerId());
                setTimestamp(message.getTimestamp());
            }}
        );
    }

    @MessageMapping("/room/{roomId}/start")
    public void startGame(@DestinationVariable String roomId, GameMessage message) {
        logger.info("Starting game in room {}", roomId);
        GameRoom room = gameRoomService.getRoom(roomId);
        if (room != null && room.isFull()) {
            room.setStatus(GameStatus.PLAYING);
            
            // Broadcast game start
            messagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                new GameMessage() {{
                    setType("GAME_START");
                    setTimestamp(message.getTimestamp());
                }}
            );
        }
    }

    @MessageMapping("/room/{roomId}/progress")
    public void updateProgress(@DestinationVariable String roomId, GameMessage message) {
        logger.debug("Progress update in room {}: Player {} at {}%", 
            roomId, message.getPlayerId(), message.getPercentage());
        
        // Broadcast progress update
        messagingTemplate.convertAndSend(
            "/topic/room/" + roomId,
            new GameMessage() {{
                setType("PLAYER_PROGRESS");
                setPlayerId(message.getPlayerId());
                setPercentage(message.getPercentage());
                setTimestamp(message.getTimestamp());
            }}
        );
    }

    @MessageMapping("/room/{roomId}/finish")
    public void finishGame(@DestinationVariable String roomId, GameMessage message) {
        logger.info("Player {} finished in room {}", message.getPlayerId(), roomId);
        GameRoom room = gameRoomService.getRoom(roomId);
        if (room != null) {
            room.setStatus(GameStatus.FINISHED);
            
            // Broadcast game finish
            messagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                new GameMessage() {{
                    setType("GAME_FINISH");
                    setPlayerId(message.getPlayerId());
                    setTimestamp(message.getTimestamp());
                }}
            );
        }
    }

    @Operation(summary = "Get the status of a game room", description = "Returns the status of the game room with the provided ID")
    @GetMapping("/api/rooms/{roomId}/status")
    public ResponseEntity<?> getRoomStatus(@PathVariable String roomId) {
        GameRoom room = gameRoomService.getRoom(roomId);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("roomId", room.getId());
        response.put("playersId", new ArrayList<>(room.getPlayersId()));
        response.put("playersName", new ArrayList<>(room.getPlayersName()));
        response.put("status", room.getStatus().toString().toLowerCase());
        response.put("createdAt", room.getStartTime() != null ? 
            Instant.ofEpochMilli(room.getStartTime()).toString() : 
            Instant.now().toString());
        response.put("maxPlayers", 2);

        return ResponseEntity.ok(response);
    }
}
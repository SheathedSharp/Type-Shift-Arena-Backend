/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-29 22:46:56
 */
package com.example.demo.controller.game;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.core.ApiResponse;
import com.example.demo.entity.PlayerProfile;
import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.model.game.GameMessage;
import com.example.demo.model.game.GameRoom;
import com.example.demo.service.game.GameRoomService;
import com.example.demo.service.user.PlayerProfileService;
import com.example.demo.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;

@Controller
public class GameRoomController {
    /*
        游戏房间控制器
        用于处理游戏房间相关的操作
    */
    
    private static final Logger logger = LoggerFactory.getLogger(GameRoomController.class); // 日志记录器

    @Autowired
    private GameRoomService gameRoomService; // 游戏房间服务

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // WebSocket消息模板

    @Autowired
    private UserService userService; // 用户服务

    @Autowired
    private PlayerProfileService playerProfileService; // 玩家信息服务

    /* 映射注解说明
        @MessageMapping: 用于处理WebSocket消息的映射，通常用于处理客户端发送的消息。@DestinationVariable: 用于从路径变量中提取参数。
        @GetMapping: 用于处理HTTP GET请求的映射，通常用于获取资源。 @PathVariable: 用于从路径变量中提取参数。
        @PostMapping: 用于处理HTTP POST请求的映射，通常用于创建资源。 @RequestBody: 用于从请求体中提取参数。
    */

    // 玩家加入房间
    @MessageMapping("/room/{roomId}/join")
    public void playerJoin(@DestinationVariable String roomId, GameMessage message) {
        logger.info("Player {} (id: {}) joining room {}", 
            message.getPlayerName(), 
            message.getPlayerId(), 
            roomId
        );
        
        gameRoomService.playerJoin(
            roomId, 
            message.getPlayerId(),
            message.getPlayerName()
        );
    }

    // 玩家离开房间
    @MessageMapping("/room/{roomId}/leave")
    public void playerLeave(@DestinationVariable String roomId, GameMessage message) {
        logger.info("Player {} (id: {}) leaving room {}", 
            message.getPlayerName(), 
            message.getPlayerId(), 
            roomId
        );
       
        gameRoomService.playerLeave(
            roomId, 
            message.getPlayerId(), 
            message.getPlayerName()
        );
    }

    // 玩家准备
    @MessageMapping("/room/{roomId}/ready")
    public void playerReady(@DestinationVariable String roomId, GameMessage message) {
        logger.info("Player {} ready status changed to {} in room {}", 
            message.getPlayerId(), 
            message.getIsReady(), 
            roomId
        );
        
        gameRoomService.playerReady(
            roomId,
            message.getPlayerId(),
            message.getPlayerName(),
            message.getIsReady()  // 传递准备状态
        );
    }

    // 获取房间信息
    @MessageMapping("/room/{roomId}/info")
    public void roomInfo(@DestinationVariable String roomId, GameMessage message) {
        logger.info("Getting room info for room {}, requested by player {}", 
            roomId, message.getPlayerId());
        
        gameRoomService.getRoomInfo(
            roomId, 
            message.getPlayerId(), 
            message.getPlayerName()
        );
    }

    // 获取所有自定义房间
    @Operation(summary = "Get all custom rooms", description = "Returns a list of all custom game rooms")
    @GetMapping("/api/rooms/custom")
    public ResponseEntity<?> getCustomRooms() {
        List<GameRoom> customRooms = gameRoomService.getCustomRooms();
        List<Map<String, Object>> roomList = customRooms.stream()
                .map(room -> {
                    Map<String, Object> roomInfo = new HashMap<>();
                    roomInfo.put("roomId", room.getId());
                    roomInfo.put("playersCount", room.getPlayersId().size());
                    roomInfo.put("status", room.getStatus().toString().toLowerCase());
                    roomInfo.put("language", room.getLanguage().toString());
                    roomInfo.put("category", room.getCategory().toString());
                    roomInfo.put("difficulty", room.getDifficulty());
                    roomInfo.put("createdAt", room.getStartTime() != null ? 
                        Instant.ofEpochMilli(room.getStartTime()).toString() : 
                        Instant.now().toString());
                    
                    // 添加玩家信息
                    List<Map<String, Object>> players = new ArrayList<>();
                    Iterator<String> playerIds = room.getPlayersId().iterator();
                    Iterator<String> playerNames = room.getPlayersName().iterator();
                    
                    while (playerIds.hasNext() && playerNames.hasNext()) {
                        String playerId = playerIds.next();
                        String playerName = playerNames.next();
                        
                        Map<String, Object> playerInfo = new HashMap<>();
                        playerInfo.put("id", playerId);
                        playerInfo.put("name", playerName);
                        
                        // 获取玩家头像
                        userService.getUserById(playerId)
                                .ifPresent(user -> {
                                    playerInfo.put("avatar", user.getImgSrc());
                                });
                        
                        // 如果没有找到用户头像，使用默认头像
                        if (!playerInfo.containsKey("avatar")) {
                            playerInfo.put("avatar", "https://api.dicebear.com/7.x/avataaars/svg?seed=" + playerName);
                        }
                        
                        // 获取玩家等级信息
                        PlayerProfile profile = playerProfileService.getPlayerProfileById(playerId);
                        if (profile != null) {
                            playerInfo.put("level", profile.getUserLevel());
                            playerInfo.put("rankScore", profile.getRankScore());
                        }
                        
                        players.add(playerInfo);
                    }
                    
                    roomInfo.put("players", players);
                    roomInfo.put("hostId", room.getHostId());
                    
                    return roomInfo;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("totalRooms", roomList.size());
        response.put("rooms", roomList);

        return ResponseEntity.ok(response);
    }

    // 获取自定义房间数量
    @Operation(summary = "Get custom rooms count", description = "Returns the number of custom game rooms")
    @GetMapping("/api/rooms/custom/count")
    public ResponseEntity<?> getCustomRoomsCount() {
        int count = gameRoomService.getCustomRoomsCount();
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
    
    // 创建自定义房间
    @Operation(summary = "Create a new game room", description = "Creates a new game room with specified settings")
    @PostMapping("/api/rooms/create")
    public ResponseEntity<?> createRoom(@RequestBody GameMessage request) {
        logger.info("Creating new room with settings - Language: {}, Category: {}, Difficulty: {}", 
            request.getLanguage(), request.getCategory(), request.getDifficulty());
        
        // 首先验证 playerId
        if (request.getPlayerId() == null || request.getPlayerId().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                ApiResponse.error(400, "Player ID is required")
            );
        }
        
        try {
            // 使用新的生成方法获取房间ID
            String roomId = gameRoomService.generateRoomId();
            
            // Create the room
            GameRoom room = gameRoomService.createRoom(
                roomId,
                TextLanguage.valueOf(request.getLanguage() != null ? request.getLanguage() : "ENGLISH"),
                TextCategory.valueOf(request.getCategory() != null ? request.getCategory() : "DAILY_CHAT"),
                request.getDifficulty() != null ? request.getDifficulty() : "EASY"
            );
            
            // Set the host ID
            room.setHostId(request.getPlayerId());
            
            // Add the host to the room
            gameRoomService.addPlayer(room, request.getPlayerId(), request.getPlayerName());
            
            // 通过WebSocket通知房间创建者
            messagingTemplate.convertAndSend(
                "/queue/room/" + request.getPlayerId() + "/info",
                new GameMessage() {{
                    setType("ROOM_CREATED");
                    setRoomId(roomId);
                    setPlayerId(request.getPlayerId());
                    setPlayerName(request.getPlayerName());
                    setIsHost(true);
                    setRoomStatus(room.getStatus().toString());
                    setLanguage(room.getLanguage().toString());
                    setCategory(room.getCategory().toString());
                    setDifficulty(room.getDifficulty());
                    setPlayersCount(room.getPlayersId().size());
                    setPlayersId(room.getPlayersId());
                    setPlayersName(room.getPlayersName());
                    setStartTime(room.getStartTime());
                    setTimestamp(System.currentTimeMillis());
                }}
            );
            
            // Create response for HTTP request
            Map<String, Object> response = new HashMap<>();
            response.put("roomId", room.getId());
            response.put("hostId", room.getHostId());
            response.put("language", room.getLanguage().toString());
            response.put("category", room.getCategory().toString());
            response.put("difficulty", room.getDifficulty());
            response.put("status", room.getStatus().toString());
            response.put("createdAt", room.getStartTime());
            response.put("playersCount", room.getPlayersId().size());
            
            return ResponseEntity.ok(ApiResponse.success("Room created successfully", response));
            
        } catch (IllegalArgumentException e) {
            logger.error("Invalid parameters for room creation", e);
            return ResponseEntity.badRequest().body(
                ApiResponse.error(400, "Invalid parameters: " + e.getMessage())
            );
        } catch (RuntimeException e) {
            logger.error("Error creating room", e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.error(500, "Error creating room: " + e.getMessage())
            );
        }
    }

    // 获取房间状态
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
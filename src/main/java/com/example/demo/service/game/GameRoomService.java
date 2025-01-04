/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-30 00:17:44
 */
/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-29 22:46:23
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2025-01-04 11:37:58
 */
package com.example.demo.service.game;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.model.game.GameMessage;
import com.example.demo.model.game.GameRoom;
import com.example.demo.model.game.GameStatus;
import com.example.demo.service.user.UserService;

import com.example.demo.service.user.UserService;
import com.example.demo.entity.User;

@Service
public class GameRoomService {
    private final Map<String, GameRoom> rooms = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private static final int MAX_PLAYERS = 2;
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ROOM_ID_LENGTH = 6;

    @Autowired
    public GameRoomService(SimpMessagingTemplate messagingTemplate, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    public GameRoom createRoom(String roomId, TextLanguage language, TextCategory category, String difficulty) {
        GameRoom room = new GameRoom(roomId);
        
        // 设置房间的语言、类型和难度属性
        room.setLanguage(language);
        room.setCategory(category);
        room.setDifficulty(difficulty);
        
        // 存储房间
        rooms.put(roomId, room);
        
        return room;
    }

    public GameRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public void playerJoin(String roomId, String playerId, String playerName) {
        GameRoom room = rooms.get(roomId);
        if (room == null) {
            // 如果房间不存在，返回 null
            return ;
        }
        
        // 检查房间是否已满
        if (isRoomFull(room)) {
            throw new IllegalStateException("Room is full");
        }
        
        // 检查玩家是否已在房间中
        if (hasPlayer(room, playerId)) {
            throw new IllegalStateException("Player already in room");
        }
        
        // 添加玩家到房间
        addPlayer(room, playerId, playerName);
        
        // 找到另一个玩家的ID
        String otherPlayerId = room.getPlayersId().stream()
            .filter(id -> !id.equals(playerId))
            .findFirst()
            .orElse(null);

        String otherPlayerName = room.getPlayersName().stream()
            .filter(name -> !name.equals(playerName))
            .findFirst()
            .orElse(null);

        if (otherPlayerId != null) {
            // 获取对手信息
            Map<String, String> opponentInfo = getOpponentInfo(room, otherPlayerId, otherPlayerName);

            GameMessage message = new GameMessage();
            message.setType("PLAYER_JOIN");
            message.setRoomId(room.getId());
            message.setPlayerId(otherPlayerId);
            message.setPlayerName(otherPlayerName);
            String avatar = !otherPlayerId.isEmpty() ? 
                userService.getUserById(otherPlayerId)
                        .map(User::getImgSrc)
                        .orElse("https://api.dicebear.com/7.x/avataaars/svg?seed=" + otherPlayerName) 
                : "";  // 使用空字符串替代null
            message.setPlayerAvatar(avatar);
            message.setOpponentId(opponentInfo.get("id"));
            message.setOpponentName(opponentInfo.get("name"));
            message.setOpponentAvatar(opponentInfo.get("avatar"));
            message.setIsHost(isPlayerHost(room, otherPlayerId));
            message.setRoomStatus(room.getStatus().toString());
            message.setPlayersId(room.getPlayersId());
            message.setPlayersName(room.getPlayersName());
            message.setPlayersCount(room.getPlayersId().size());
            message.setTimestamp(System.currentTimeMillis());
        

            // 通知另一个玩家
            messagingTemplate.convertAndSend(
                "/queue/room/" + otherPlayerId + "/info",
                message
            );
        }
    }

    public void playerLeave(String roomId, String playerId, String playerName) {
        GameRoom room = rooms.get(roomId);
        if (room != null) {
            if (removePlayer(room, playerId, playerName)) {          // 如果房间为空
                rooms.remove(roomId); // 移除房间
            } else {                // 如果房间不空
                // 找到另一个玩家的ID
                String otherPlayerId = room.getPlayersId().stream()
                    .filter(id -> !id.equals(playerId))
                    .findFirst()
                    .orElse(null);

                String otherPlayerName = room.getPlayersName().stream()
                    .filter(name -> !name.equals(playerName))
                    .findFirst()
                    .orElse(null);

                if (otherPlayerId != null) {
                    GameMessage message = new GameMessage();
                    message.setType("PLAYER_LEAVE");
                    message.setRoomId(room.getId());
                    message.setPlayerId(otherPlayerId);
                    message.setPlayerName(otherPlayerName);
                    String avatar = !otherPlayerId.isEmpty() ? 
                        userService.getUserById(otherPlayerId)
                                .map(User::getImgSrc)
                                .orElse("https://api.dicebear.com/7.x/avataaars/svg?seed=" + otherPlayerName) 
                        : "";  // 使用空字符串替代null
                    message.setPlayerAvatar(avatar);
                    message.setOpponentId("");
                    message.setOpponentName("");
                    message.setOpponentAvatar("");
                    message.setPlayersId(room.getPlayersId());
                    message.setPlayersName(room.getPlayersName());
                    message.setRoomStatus(room.getStatus().toString());
                    message.setIsHost(isPlayerHost(room, otherPlayerId));
                    message.setTimestamp(System.currentTimeMillis());

                    // 通知另一个玩家
                    messagingTemplate.convertAndSend(
                        "/queue/room/" + otherPlayerId + "/info",
                        message
                    );
                }

                room.setStatus(GameStatus.WAITING);
            }
        }

        
    }

    public boolean roomExists(String roomId) {
        return rooms.containsKey(roomId);
    }

    public boolean addPlayer(GameRoom room, String playerId, String playerName) {
        if (!isRoomFull(room)) {
            room.getPlayersId().add(playerId);
            room.getPlayersName().add(playerName);
            if (isRoomFull(room)) {
                room.setStatus(GameStatus.READY);
            }else {
                room.setStatus(GameStatus.WAITING);
            }
            return true;
        }
        return false;
    }

    public boolean removePlayer(GameRoom room, String playerId, String playerName) {
        // 更新房主        
        if (isPlayerHost(room, playerId)) {
            room.setHostId(room.getPlayersId().stream()
                .filter(id -> !id.equals(playerId))
                .findFirst()
                .orElse(null));
        }
        room.getPlayersId().remove(playerId);
        room.getPlayersName().remove(playerName);
        room.setStatus(GameStatus.WAITING);
        return room.getPlayersId().isEmpty();
    }

    public boolean isRoomFull(GameRoom room) {
        return room.getPlayersId().size() >= MAX_PLAYERS;
    }

    public boolean isRoomEmpty(GameRoom room) {
        return room.getPlayersId().isEmpty();
    }

    public boolean isPlayerHost(GameRoom room, String playerId) {
        return room.getHostId() != null && room.getHostId().equals(playerId);
    }

    public boolean hasPlayer(GameRoom room, String playerId) {
        return room.getPlayersId().contains(playerId);
    }

    // 获取对手信息
    private Map<String, String> getOpponentInfo(GameRoom room, String currentPlayerId, String currentPlayerName) {
        Map<String, String> opponentInfo = new ConcurrentHashMap<>();
        
        if (room == null || currentPlayerId == null) {
            // 如果房间或当前玩家ID为空，返回空Map
            return opponentInfo;
        }

        // 找出对手ID
        String opponentId = room.getPlayersId().stream()
            .filter(id -> !id.equals(currentPlayerId))
            .findFirst()
            .orElse("");  // 使用空字符串替代null
            
        // 找出对手名称
        String opponentName = !opponentId.isEmpty() ? 
            room.getPlayersName().stream()
                .filter(name -> !name.equals(currentPlayerName))
                .findFirst()
                .orElse("") : "";  // 使用空字符串替代null
                
        // 获取对手头像
        String opponentAvatar = !opponentId.isEmpty() ? 
            userService.getUserById(opponentId)
                    .map(User::getImgSrc)
                    .orElse("https://api.dicebear.com/7.x/avataaars/svg?seed=" + opponentName) 
            : "";  // 使用空字符串替代null
            
        // 只有在值不为空时才放入Map
        if (!opponentId.isEmpty()) opponentInfo.put("id", opponentId);
        if (!opponentName.isEmpty()) opponentInfo.put("name", opponentName);
        if (!opponentAvatar.isEmpty()) opponentInfo.put("avatar", opponentAvatar);
        
        return opponentInfo;
    }

    public void getRoomInfo(String roomId, String requestPlayerId, String requestPlayerName) {
        GameRoom room = getRoom(roomId);
        if (room == null) {
            return ;
        }

        // 获取对手信息
        Map<String, String> opponentInfo = getOpponentInfo(room, requestPlayerId, requestPlayerName);

        // 构建房间信息消息
        GameMessage roomInfo = new GameMessage();
        roomInfo.setType("GAME_INFO");
        roomInfo.setRoomId(roomId);
        roomInfo.setPlayerId(requestPlayerId);
        roomInfo.setPlayerName(requestPlayerName);
        
        // 获取请求玩家的头像
        String playerAvatar = userService.getUserById(requestPlayerId)
                .map(User::getImgSrc)
                .orElse("https://api.dicebear.com/7.x/avataaars/svg?seed=" + requestPlayerName);
        roomInfo.setPlayerAvatar(playerAvatar);
        
        // 设置对手信息
        roomInfo.setOpponentId(opponentInfo.get("id"));
        roomInfo.setOpponentName(opponentInfo.get("name"));
        roomInfo.setOpponentAvatar(opponentInfo.get("avatar"));
        
        roomInfo.setLanguage(room.getLanguage().toString());
        roomInfo.setCategory(room.getCategory().toString());
        roomInfo.setDifficulty(room.getDifficulty());
        roomInfo.setRoomStatus(room.getStatus().toString());
        roomInfo.setPlayersCount(room.getPlayersId().size());
        roomInfo.setTargetText(room.getTargetText());
        roomInfo.setStartTime(room.getStartTime());
        roomInfo.setTimestamp(System.currentTimeMillis());
        roomInfo.setIsHost(isPlayerHost(room, requestPlayerId));        // 设置是否为房主

        // 发送房间信息给请求的玩家
        messagingTemplate.convertAndSend(
            "/queue/room/" + requestPlayerId + "/info",
            roomInfo
        );
    }

    public void playerReady(String roomId, String playerId, String playerName, Boolean isReady) {
        GameRoom room = getRoom(roomId);
        if (room != null) {            
            // 找到另一个玩家的ID
            String otherPlayerId = room.getPlayersId().stream()
                .filter(id -> !id.equals(playerId))
                .findFirst()
                .orElse(null);
            
            if (otherPlayerId != null) {
                // 通知另一个玩家
                messagingTemplate.convertAndSend(
                    "/queue/room/" + otherPlayerId + "/info",
                    new GameMessage() {{
                        setType("PLAYER_READY");
                        setPlayerId(playerId);          // 准备的玩家ID
                        setPlayerName(playerName);      // 准备的玩家名称
                        setRoomId(roomId);
                        setIsReady(isReady);  // 设置准备状态
                        setRoomStatus(room.getStatus().toString());
                        setTimestamp(System.currentTimeMillis());
                    }}
                );
            }
        }
    }

    public List<GameRoom> getCustomRooms() {
        return rooms.values().stream()
                .filter(room -> !room.isRanked())
                .collect(Collectors.toList());
    }

    public int getCustomRoomsCount() {
        return (int) rooms.values().stream()
                .filter(room -> !room.isRanked())
                .count();
    }

    public String generateRoomId() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        
        while (true) {
            // 生成6位随机字符的房间ID
            for (int i = 0; i < ROOM_ID_LENGTH; i++) {
                int index = random.nextInt(CHARS.length());
                sb.append(CHARS.charAt(index));
            }
            
            String roomId = sb.toString();
            // 确保生成的ID是唯一的
            if (!rooms.containsKey(roomId)) {
                return roomId;
            }
            sb.setLength(0); // 清空重试
        }
    }
}
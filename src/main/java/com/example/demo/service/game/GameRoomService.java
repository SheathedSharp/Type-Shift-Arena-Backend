/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-29 22:46:23
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-14 07:59:08
 */
package com.example.demo.service.game;

import com.example.demo.model.game.GameRoom;
import com.example.demo.model.game.GameStatus;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.entity.enums.TextCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameRoomService {
    private final Map<String, GameRoom> rooms = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(GameRoomService.class);
    private static final int MAX_PLAYERS = 2;

    @Autowired
    private GameTextService gameTextService;

    // public GameRoom createRoom(String roomId) {
    //     GameRoom room = new GameRoom(roomId);
    //     String targetText = gameTextService.getRandomText(difficulty, category);
    //     room.setTargetText(targetText);
    //     rooms.put(roomId, room);
    //     return room;
    // }
    public GameRoom createRoom(String roomId, TextLanguage language, TextCategory category, String difficulty) {
        GameRoom room = new GameRoom(roomId);
        String targetText = gameTextService.getRandomText(language, category, difficulty);
        room.setTargetText(targetText);
        rooms.put(roomId, room);
        return room;
    }

    // 添加一个使用默认参数的重载方法
    public GameRoom createRoom(String roomId) {
        return createRoom(
            roomId,
            TextLanguage.ENGLISH,  // 默认使用英语
            TextCategory.DAILY_CHAT,  // 默认使用日常对话
            "EASY"  // 默认使用简单难度
        );
    }

    public GameRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public GameRoom joinRoom(String roomId, String playerId, String playerName, 
                            TextLanguage language, TextCategory category, String difficulty) {
        GameRoom room = rooms.get(roomId);
        if (room == null) {
            // 使用传入的参数创建房间
            room = createRoom(roomId, language, category, difficulty);
        }
        
        if (addPlayer(room, playerId, playerName)) {
            if (isRoomFull(room)) {
                setGameStatus(room, GameStatus.READY);
            }
        }
        
        return room;
    }

    // 添加一个使用默认参数的重载方法
    public GameRoom joinRoom(String roomId, String playerId, String playerName) {
        return joinRoom(
            roomId,
            playerId,
            playerName,
            TextLanguage.ENGLISH,  // 默认使用英语
            TextCategory.DAILY_CHAT,  // 默认使用日常对话
            "EASY"  // 默认使用简单难度
        );
    }

    public void leaveRoom(String roomId, String playerId, String playerName) {
        GameRoom room = rooms.get(roomId);
        if (room != null) {
            if (removePlayer(room, playerId, playerName)) {
                rooms.remove(roomId);
            } else {
                setGameStatus(room, GameStatus.WAITING);
            }
        }
    }

    public boolean roomExists(String roomId) {
        return rooms.containsKey(roomId);
    }

    public void updateGameStatus(String roomId, GameStatus status) {
        GameRoom room = rooms.get(roomId);
        if (room != null) {
            setGameStatus(room, status);
        }
    }

    public void setGameStatus(GameRoom room, GameStatus status) {
        room.setStatus(status);
        if (status == GameStatus.PLAYING) {
            room.setStartTime(System.currentTimeMillis());
        }
    }

    public boolean addPlayer(GameRoom room, String playerId, String playerName) {
        if (!isRoomFull(room)) {
            room.getPlayersId().add(playerId);
            room.getPlayersName().add(playerName);
            return true;
        }
        return false;
    }

    public boolean removePlayer(GameRoom room, String playerId, String playerName) {
        room.getPlayersId().remove(playerId);
        room.getPlayersName().remove(playerName);
        return room.getPlayersId().isEmpty();
    }

    public boolean isRoomFull(GameRoom room) {
        return room.getPlayersId().size() >= MAX_PLAYERS;
    }

    public boolean isRoomEmpty(GameRoom room) {
        return room.getPlayersId().isEmpty();
    }

    public boolean isPlayerHost(GameRoom room, String playerId) {
        return !room.getPlayersId().isEmpty() && 
               room.getPlayersId().iterator().next().equals(playerId);
    }

    public boolean hasPlayer(GameRoom room, String playerId) {
        return room.getPlayersId().contains(playerId);
    }
}
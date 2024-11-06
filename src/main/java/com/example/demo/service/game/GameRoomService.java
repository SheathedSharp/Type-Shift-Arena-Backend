/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-29 22:46:23
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-06 21:29:07
 */
package com.example.demo.service.game;

import com.example.demo.model.game.GameRoom;
import com.example.demo.model.game.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameRoomService {
    private final Map<String, GameRoom> rooms = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(GameRoomService.class);

    public GameRoom createRoom(String roomId) {
        GameRoom room = new GameRoom(roomId);
        rooms.put(roomId, room);
        return room;
    }

    public GameRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public GameRoom joinRoom(String roomId, String playerId, String playerName) {
        GameRoom room = rooms.get(roomId);
        if (room == null) {
            room = createRoom(roomId);
        }
        
        room.addPlayer(playerId, playerName);
        
        if (room.isFull()) {
            room.setStatus(GameStatus.READY);
        }
        
        return room;
    }

    public void leaveRoom(String roomId, String playerId, String playerName) {
        GameRoom room = rooms.get(roomId);
        if (room != null) {
            room.getPlayersId().remove(playerId);
            room.getPlayersName().remove(playerName);
            
            if (room.getPlayersId().isEmpty()) {
                rooms.remove(roomId);
            } else {
                room.setStatus(GameStatus.WAITING);
            }
        }
    }

    public boolean roomExists(String roomId) {
        return rooms.containsKey(roomId);
    }

    public void updateGameStatus(String roomId, GameStatus status) {
        GameRoom room = rooms.get(roomId);
        if (room != null) {
            room.setStatus(status);
        }
    }
}
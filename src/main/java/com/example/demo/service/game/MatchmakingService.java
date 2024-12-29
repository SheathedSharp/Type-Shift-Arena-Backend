/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-14 17:00:29
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-15 19:31:21
 */
package com.example.demo.service.game;

import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.entity.enums.TextCategory;
import com.example.demo.model.game.GameMessage;
import com.example.demo.model.game.MatchRequest;
import com.example.demo.model.game.GameRoom;
import com.example.demo.model.game.GameStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class MatchmakingService {
    private final Map<String, Queue<MatchRequest>> matchmakingQueues = new ConcurrentHashMap<>();
    private final GameRoomService gameRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MatchmakingService(GameRoomService gameRoomService, SimpMessagingTemplate messagingTemplate) {
        this.gameRoomService = gameRoomService;
        this.messagingTemplate = messagingTemplate;
    }

    public void addToQueue(MatchRequest request) {
        String queueKey = generateQueueKey(request.getLanguage(), request.getCategory(), request.getDifficulty());
        Queue<MatchRequest> queue = matchmakingQueues.computeIfAbsent(queueKey, k -> new ConcurrentLinkedQueue<>());
        
        // 检查队列中是否有匹配的玩家
        MatchRequest opponent = findMatch(queue, request);
        if (opponent != null) {
            // 找到匹配，创建房间
            createMatchedRoom(opponent, request);
        } else {
            // 没找到匹配，加入队列等待
            queue.offer(request);
        }
    }

    public void removeFromQueue(MatchRequest request) {
        String queueKey = generateQueueKey(request.getLanguage(), request.getCategory(), request.getDifficulty());
        Queue<MatchRequest> queue = matchmakingQueues.get(queueKey);
        
        if (queue != null) {
            // 移除该玩家的匹配请求
            queue.removeIf(req -> req.getPlayerId().equals(request.getPlayerId()));
            
            // 如果队列为空，从map中移除该队列
            if (queue.isEmpty()) {
                matchmakingQueues.remove(queueKey);
            }
            
            // 通知玩家已取消匹配
            messagingTemplate.convertAndSend(
                "/queue/matchmaking/" + request.getPlayerId(),
                new GameMessage() {{
                    setType("MATCH_CANCELLED");
                    setPlayerId(request.getPlayerId());
                    setTimestamp(System.currentTimeMillis());
                }}
            );
        }
    }

    public String generateQueueKey(TextLanguage language, TextCategory category, String difficulty) {
        return language + "_" + category + "_" + difficulty;
    }

    public MatchRequest findMatch(Queue<MatchRequest> queue, MatchRequest newRequest) {
        return queue.poll();
    }

    public void createMatchedRoom(MatchRequest player1, MatchRequest player2) {
        String roomId = UUID.randomUUID().toString();
        
        // 创建房间并添加两个玩家
        GameRoom room = gameRoomService.createRoom(
            roomId,
            player1.getLanguage(),
            player1.getCategory(),
            player1.getDifficulty()
        );
        
        // 设置player1为房主
        room.setHostId(player1.getPlayerId());
        
        // 添加两个玩家到房间
        gameRoomService.addPlayer(room, player1.getPlayerId(), player1.getPlayerName());
        gameRoomService.addPlayer(room, player2.getPlayerId(), player2.getPlayerName());
        
        // 设置房间状态为准备就绪
        // gameRoomService.setGameStatus(room, GameStatus.READY);
        
        // 通知两个玩家已匹配成功，并发送房间和对手信息
        notifyMatchFound(player1, player2, roomId, room, true);  // player1是房主
        notifyMatchFound(player2, player1, roomId, room, false); // player2不是房主
    }

    private void notifyMatchFound(MatchRequest player, MatchRequest opponent, 
                                String roomId, GameRoom room, boolean isHost) {
        messagingTemplate.convertAndSend(
            "/queue/matchmaking/" + player.getPlayerId(),
            new GameMessage() {{
                setType("MATCH_FOUND");
                setRoomId(roomId);
                setPlayerId(player.getPlayerId());
                setPlayerName(player.getPlayerName());
                setPlayerAvatar(player.getPlayerAvatar());
                setOpponentId(opponent.getPlayerId());
                setOpponentName(opponent.getPlayerName());
                setOpponentAvatar(opponent.getPlayerAvatar());
                setTargetText(room.getTargetText());
                setLanguage(room.getLanguage().toString());
                setCategory(room.getCategory().toString());
                setDifficulty(room.getDifficulty());
                setMessage(String.format("找到对手: %s", opponent.getPlayerName()));
                setRoomStatus(GameStatus.WAITING.toString());
                setPlayersCount(room.getPlayersId().size());
                setStartTime(room.getStartTime());
                setTimestamp(System.currentTimeMillis());
                setIsHost(isHost);  // 添加是否为房主的标识
            }}
        );
    }

    public Map<String, Queue<MatchRequest>> getMatchmakingQueues() {
        return new ConcurrentHashMap<>(matchmakingQueues);
    }
}
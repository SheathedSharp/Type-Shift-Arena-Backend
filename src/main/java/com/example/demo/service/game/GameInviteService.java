/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2025-01-06 17:03:08
 */
package com.example.demo.service.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.core.ApiResponse;
import com.example.demo.model.game.GameMessage;
import com.example.demo.model.game.GameRoom;
import com.example.demo.service.user.FriendService;
import com.example.demo.service.user.UserOnlineService;
import com.example.demo.service.user.UserService;

@Service
@Transactional
public class GameInviteService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private UserOnlineService userOnlineService;
    
    @Autowired
    private FriendService friendService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private GameRoomService gameRoomService;
    
    public void sendGameInvite(String senderId, String receiverId, String roomId) {
        // 1. 验证接收者是否在线
        if (!userOnlineService.isUserOnline(receiverId)) {
            throw new RuntimeException("Friend is offline");
        }
        
        // 2. 验证是否为好友关系
        if (!friendService.areFriends(senderId, receiverId)) {
            throw new RuntimeException("Not friends");
        }
        
        // 3. 发送邀请消息
        GameMessage inviteMessage = new GameMessage();
        inviteMessage.setType("GAME_INVITE");
        inviteMessage.setPlayerId(senderId);
        inviteMessage.setPlayerName(userService.getUserById(senderId).get().getUsername());
        inviteMessage.setPlayerAvatar(userService.getUserById(senderId).get().getImgSrc());
        inviteMessage.setRoomId(roomId);
        inviteMessage.setTimestamp(System.currentTimeMillis());

        GameRoom room = gameRoomService.getRoom(roomId);
        inviteMessage.setCategory(room.getCategory().toString());
        inviteMessage.setDifficulty(room.getDifficulty());
        inviteMessage.setLanguage(room.getLanguage().toString());
        inviteMessage.setRoomStatus(room.getStatus().toString());
        inviteMessage.setPlayersCount(room.getPlayersId().size());
        inviteMessage.setStartTime(room.getStartTime());

        messagingTemplate.convertAndSend(
            "/queue/messages/" + receiverId,
            ApiResponse.success("Game invitation received", inviteMessage)
        );
    }
}


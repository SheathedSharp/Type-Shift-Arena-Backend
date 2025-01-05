/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-07 08:27:08
 */
package com.example.demo.controller.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.core.ApiResponse;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.MessageStatus;
import com.example.demo.model.dto.FriendDTO;
import com.example.demo.service.user.FriendService;
import com.example.demo.service.user.MessageService;


@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private MessageService messageService;

    // 获取好友列表
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Set<FriendDTO>>> getFriends(@PathVariable String userId) {
        Set<User> friends = friendService.getFriends(userId);
        Set<FriendDTO> friendDTOs = friends.stream()
                .map(FriendDTO::new)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(ApiResponse.success("Successfully retrieved friends", friendDTOs));
    }

    // 搜索用户（潜在好友）
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FriendDTO>>> searchUsers(
            @RequestParam String username,
            @RequestParam String currentUserId) {
        List<User> users = friendService.searchFriends(username);
        List<FriendDTO> userDTOs = users.stream()
                .filter(user -> !user.getId().equals(currentUserId)) // 排除自己
                .filter(user -> !friendService.areFriends(currentUserId, user.getId())) // 排除已是好友的用户
                .map(FriendDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Search results", userDTOs));
    }

    // 发送好友请求
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<Void>> sendFriendRequest(
            @RequestParam String senderId,
            @RequestParam String receiverId) {
        // 检查是否已经是好友
        if (friendService.areFriends(senderId, receiverId)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "Already friends"));
        }

        // 检查是否已经有待处理的请求
        if (friendService.hasPendingFriendRequest(senderId, receiverId)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "Friend request already pending"));
        }

        messageService.sendFriendRequest(senderId, receiverId);
        return ResponseEntity.ok(ApiResponse.success("Friend request sent successfully", null));
    }

    // 处理好友请求
    @PostMapping("/request/{messageId}")
    public ResponseEntity<ApiResponse<Void>> handleFriendRequest(
            @PathVariable String messageId,
            @RequestParam MessageStatus status) {
        try {
            messageService.handleFriendRequest(messageId, status);
            return ResponseEntity.ok(ApiResponse.success("Friend request handled successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "Failed to handle friend request: " + e.getMessage()));
        }
    }

    // 删除好友
    @DeleteMapping("/{userId}/{friendId}")
    public ResponseEntity<ApiResponse<Void>> removeFriend(
            @PathVariable String userId,
            @PathVariable String friendId) {
        friendService.removeFriend(userId, friendId);
        return ResponseEntity.ok(ApiResponse.success("Friend removed successfully", null));
    }

    // 获取在线好友列表
    @GetMapping("/{userId}/online")
    public ResponseEntity<ApiResponse<List<FriendDTO>>> getOnlineFriends(
            @PathVariable String userId) {
        List<User> onlineFriends = friendService.getOnlineFriends(userId);
        List<FriendDTO> onlineFriendDTOs = onlineFriends.stream()
                .map(FriendDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Online friends retrieved successfully", onlineFriendDTOs));
    }
}

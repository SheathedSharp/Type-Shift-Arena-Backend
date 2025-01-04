/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-07 08:27:08
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2025-01-04 11:33:42
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
import com.example.demo.model.dto.FriendDTO;
import com.example.demo.service.user.FriendService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @Operation(summary = "Get user's friends", description = "Returns a list of friends for the specified user")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Set<FriendDTO>>> getFriends(@PathVariable String userId) {
        Set<User> friends = friendService.getFriends(userId);
        Set<FriendDTO> friendDTOs = friends.stream()
            .map(FriendDTO::new)
            .collect(Collectors.toSet());
        return ResponseEntity.ok(ApiResponse.success("Successfully retrieved friends list", friendDTOs));
    }

    @Operation(summary = "Search friends", description = "Search users by username")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FriendDTO>>> searchFriends(@RequestParam String username) {
        List<User> users = friendService.searchFriends(username);
        List<FriendDTO> friendDTOs = users.stream()
            .map(FriendDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Successfully searched users", friendDTOs));
    }

    @Operation(summary = "Add friend", description = "Add a new friend relationship")
    @PostMapping("/{userId}/add/{friendId}")
    public ResponseEntity<ApiResponse<Void>> addFriend(@PathVariable String userId, @PathVariable String friendId) {
        friendService.addFriend(userId, friendId);
        return ResponseEntity.ok(ApiResponse.success("Friend added successfully", null));
    }

    @Operation(summary = "Remove friend", description = "Remove a friend relationship")
    @DeleteMapping("/{userId}/remove/{friendId}")
    public ResponseEntity<ApiResponse<Void>> removeFriend(@PathVariable String userId, @PathVariable String friendId) {
        friendService.removeFriend(userId, friendId);
        return ResponseEntity.ok(ApiResponse.success("Friend removed successfully", null));
    }
} 
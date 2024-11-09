/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-07 08:27:08
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-07 09:08:37
 */
package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.FriendService;
import com.example.demo.dto.FriendDTO;
//这里对应ApiResponse的import语句，我这里显示这样改是对的
import com.example.demo.core.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @Operation(summary = "Get user's friends", description = "Returns a list of friends for the specified user")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Set<FriendDTO>>> getFriends(@PathVariable Long userId) {
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
    public ResponseEntity<ApiResponse<Void>> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        friendService.addFriend(userId, friendId);
        return ResponseEntity.ok(ApiResponse.success("Friend added successfully", null));
    }

    @Operation(summary = "Remove friend", description = "Remove a friend relationship")
    @DeleteMapping("/{userId}/remove/{friendId}")
    public ResponseEntity<ApiResponse<Void>> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        friendService.removeFriend(userId, friendId);
        return ResponseEntity.ok(ApiResponse.success("Friend removed successfully", null));
    }
} 
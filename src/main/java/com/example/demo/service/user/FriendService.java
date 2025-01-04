/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-07 08:26:55
 */
package com.example.demo.service.user;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class FriendService {
    @Autowired
    private UserRepository userRepository;

    public Set<User> getFriends(String userId) {
        return userRepository.findById(userId)
                .map(User::getFriends)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> searchFriends(String username) {
        return userRepository.findByUsernameContaining(username);
    }

    public void addFriend(String userId, String friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        user.getFriends().add(friend);
        friend.getFriends().add(user);  // Bidirectional friendship

        userRepository.save(user);
        userRepository.save(friend);
    }

    public void removeFriend(String userId, String friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userRepository.save(user);
        userRepository.save(friend);
    }
} 
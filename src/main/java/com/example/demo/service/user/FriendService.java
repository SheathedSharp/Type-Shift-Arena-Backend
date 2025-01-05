/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-07 08:26:55
 */
package com.example.demo.service.user;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.User;
import com.example.demo.entity.enums.MessageStatus;
import com.example.demo.entity.enums.MessageType;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;

@Service
public class FriendService {
    private static final Logger logger = LoggerFactory.getLogger(FriendService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Lazy
    @Autowired
    private UserOnlineService userOnlineService;

    @Transactional(readOnly = true)
    public Set<User> getFriends(String userId) {
        try {
            if (userId == null) {
                logger.warn("Attempted to get friends for null userId");
                return Collections.emptySet();
            }

            return userRepository.findById(userId)
                    .map(user -> {
                        Set<User> friends = user.getFriends();
                        logger.debug("Retrieved {} friends for user {}", friends.size(), userId);
                        return friends;
                    })
                    .orElseGet(() -> {
                        logger.warn("User not found with ID: {}", userId);
                        return Collections.emptySet();
                    });
        } catch (Exception e) {
            logger.error("Error retrieving friends for user {}: {}", userId, e.getMessage());
            return Collections.emptySet();
        }
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

    // 检查是否已经是好友
    public boolean areFriends(String userId1, String userId2) {
        User user = userRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFriends().stream()
                .anyMatch(friend -> friend.getId().equals(userId2));
    }

    // 检查是否有待处理的好友请求
    public boolean hasPendingFriendRequest(String senderId, String receiverId) {
        return messageRepository.existsBySenderIdAndReceiverIdAndTypeAndStatus(
            senderId, receiverId, MessageType.FRIEND_REQUEST, MessageStatus.UNREAD);
    }

    // 获取在线好友列表
    public List<User> getOnlineFriends(String userId) {
        Set<User> friends = getFriends(userId);
        return friends.stream()
                .filter(friend -> userOnlineService.isUserOnline(friend.getId()))
                .collect(Collectors.toList());
    }
} 
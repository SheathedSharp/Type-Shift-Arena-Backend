/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2025-01-04 12:15:41
 */
package com.example.demo.service.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.core.ApiResponse;
import com.example.demo.entity.User;

@Service
public class UserOnlineService {
    
    private static final String ONLINE_USER_PREFIX = "online:user:";
    private static final long ONLINE_TIMEOUT = 5; // 5分钟没有活动则视为离线
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Lazy
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Lazy
    @Autowired
    private FriendService friendService;
    
    private static final Logger logger = LoggerFactory.getLogger(UserOnlineService.class);
    
    // 更新用户在线状态
    public void updateUserStatus(String userId) {
        String key = ONLINE_USER_PREFIX + userId;
        redisTemplate.opsForValue().set(key, "online", ONLINE_TIMEOUT, TimeUnit.MINUTES);
    }
    
    // 检查用户是否在线
    public boolean isUserOnline(String userId) {
        String key = ONLINE_USER_PREFIX + userId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    // 用户下线
    public void userOffline(String userId) {
        String key = ONLINE_USER_PREFIX + userId;
        redisTemplate.delete(key);
    }
    
    // 当用户上线/下线时通知其所有好友
    public void notifyFriendsStatusChange(String userId, boolean isOnline) {
        try {
            // 首先检查用户是否存在
            if (userId == null) {
                logger.warn("Attempted to notify friends of null userId");
                return;
            }

            // 获取该用户的所有好友
            Set<User> friends = friendService.getFriends(userId);
            if (friends.isEmpty()) {
                logger.debug("User {} has no friends to notify", userId);
                return;
            }
            
            // 构建状态更新消息
            Map<String, Object> statusUpdate = new HashMap<>();
            statusUpdate.put("userId", userId);
            statusUpdate.put("online", isOnline);
            statusUpdate.put("timestamp", System.currentTimeMillis());
            
            // 通知每个好友
            friends.forEach(friend -> {
                try {
                    if (friend != null && friend.getId() != null) {
                        messagingTemplate.convertAndSend(
                            "/user/" + friend.getId() + "/queue/friends/status",
                            ApiResponse.success("Friend status updated", statusUpdate)
                        );
                        logger.debug("Notified user {} about status change of user {}", friend.getId(), userId);
                        logger.debug("Message sent to: /user/" + friend.getId() + "/queue/friends/status");
                    }
                } catch (Exception e) {
                    logger.warn("Failed to notify user {} about status change of user {}: {}", 
                        friend.getId(), userId, e.getMessage());
                }
            });
        } catch (Exception e) {
            logger.error("Error while notifying friends about user status change for user {}: {}", 
                userId, e.getMessage(), e);
        }
    }
} 
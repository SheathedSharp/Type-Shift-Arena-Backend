/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2025-01-04 12:18:15
 */
package com.example.demo.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.example.demo.service.user.UserOnlineService;

@Component
public class UserActivityInterceptor implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UserActivityInterceptor.class);

    @Autowired
    private UserOnlineService userOnlineService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
            
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                String userId = accessor.getUser().getName();
                if (userId != null) {
                    userOnlineService.updateUserStatus(userId);
                    userOnlineService.notifyFriendsStatusChange(userId, true);
                }
            } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                String userId = accessor.getUser().getName();
                if (userId != null) {
                    userOnlineService.userOffline(userId);
                    userOnlineService.notifyFriendsStatusChange(userId, false);
                }
            }
        } catch (Exception e) {
            // 记录错误但不中断连接/断开连接的流程
            logger.error("Error in UserActivityInterceptor", e);
        }
        
        return message;
    }
}

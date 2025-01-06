/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2025-01-04 11:53:11
 */
package com.example.demo.service.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.core.ApiResponse;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.MessageStatus;
import com.example.demo.entity.enums.MessageType;
import com.example.demo.model.dto.MessageDTO;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FriendService friendService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 发送好友请求
    public Message sendFriendRequest(String senderId, String receiverId) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setType(MessageType.FRIEND_REQUEST);
        message.setStatus(MessageStatus.UNREAD);
        message.setContent("请求添加您为好友");
        
        Message savedMessage = messageRepository.save(message);
        
        // 通过WebSocket通知接收者
        notifyUser(receiverId, MessageType.FRIEND_REQUEST, savedMessage);
        
        return savedMessage;
    }

    // 处理好友请求
    public void handleFriendRequest(String messageId, MessageStatus status) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found"));
            
        message.setStatus(status);
        messageRepository.save(message);
        
        if (MessageStatus.ACCEPTED.equals(status)) {
            // 添加好友关系
            friendService.addFriend(message.getSenderId(), message.getReceiverId());
            
            // 通知发送者请求被接受
            notifyUser(message.getSenderId(), MessageType.FRIEND_REQUEST, message);
        } else {
            // 通知发送者请求被拒绝
            notifyUser(message.getSenderId(), MessageType.FRIEND_REQUEST, message);
        }
    }

    // 获取用户未读消息
    public List<MessageDTO> getUnreadMessages(String userId) {
        try {
            List<Message> messages = messageRepository.findByReceiverIdAndStatus(userId, MessageStatus.UNREAD);
            return messages.stream()
                .map(message -> {
                    User sender = userRepository.findById(message.getSenderId())
                        .orElse(null);
                    User receiver = userRepository.findById(message.getReceiverId())
                        .orElse(null);
                    return new MessageDTO(message, sender, receiver);
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve unread messages: " + e.getMessage());
        }
    }

    // 标记消息为已读
    public void markAsRead(String messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found"));
            
        message.setStatus(MessageStatus.READ);
        messageRepository.save(message);
    }

    // 通过WebSocket通知用户
    private void notifyUser(String userId, MessageType type, Message message) {
        User sender = userRepository.findById(message.getSenderId())
            .orElse(null);
        User receiver = userRepository.findById(message.getReceiverId())
            .orElse(null);
        MessageDTO messageDTO = new MessageDTO(message, sender, receiver);
        
        messagingTemplate.convertAndSend(
            "/queue/messages/" + userId,
            ApiResponse.success(type.getDescription(), messageDTO)
        );
    }
} 
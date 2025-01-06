/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2025-01-05 12:37:25
 */
package com.example.demo.model.dto;

import java.time.LocalDateTime;

import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.MessageStatus;
import com.example.demo.entity.enums.MessageType;

import lombok.Data;

@Data
public class MessageDTO {
    private String id;
    private MessageType type;
    private MessageStatus status;
    private String content;
    private LocalDateTime createdAt;
    
    // 发送者信息
    private String senderId;
    private String senderName;
    private String senderAvatar;
    
    // 接收者信息
    private String receiverId;
    private String receiverName;
    private String receiverAvatar;
    
    public MessageDTO(Message message, User sender, User receiver) {
        this.id = message.getId();
        this.type = message.getType();
        this.status = message.getStatus();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        
        if (sender != null) {
            this.senderId = sender.getId();
            this.senderName = sender.getUsername();
            this.senderAvatar = sender.getImgSrc();
        }
        
        if (receiver != null) {
            this.receiverId = receiver.getId();
            this.receiverName = receiver.getUsername();
            this.receiverAvatar = receiver.getImgSrc();
        }
    }
}
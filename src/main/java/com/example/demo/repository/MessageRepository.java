/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2025-01-04 12:08:31
 */
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Message;
import com.example.demo.entity.enums.MessageStatus;
import com.example.demo.entity.enums.MessageType;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    // 获取用户的所有未读消息
    List<Message> findByReceiverIdAndStatus(String receiverId, MessageStatus status);
    
    // 获取用户的所有消息
    List<Message> findByReceiverId(String receiverId);
    
    // 检查是否存在待处理的好友请求
    boolean existsBySenderIdAndReceiverIdAndTypeAndStatus(
        String senderId, 
        String receiverId, 
        MessageType type, 
        MessageStatus status
    );
    
    // 获取特定类型的消息
    List<Message> findByReceiverIdAndType(String receiverId, MessageType type);
    
    // 获取发送者和接收者之间的所有消息
    List<Message> findBySenderIdAndReceiverId(String senderId, String receiverId);
    
    // 删除过期的消息（可选，用于系统清理）
    void deleteByTypeAndStatusAndCreatedAtBefore(
        MessageType type, 
        MessageStatus status, 
        java.time.LocalDateTime date
    );
} 
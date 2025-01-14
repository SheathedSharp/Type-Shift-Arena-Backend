/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2025-01-04 11:50:46
 */
package com.example.demo.entity;

import java.time.LocalDateTime;

import com.example.demo.entity.enums.MessageStatus;
import com.example.demo.entity.enums.MessageType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "sender_id", nullable = false)
    private String senderId;

    @Column(name = "receiver_id", nullable = false)
    private String receiverId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Column(length = 500)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 
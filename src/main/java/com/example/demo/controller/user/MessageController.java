/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2025-01-04 11:52:06
 */
package com.example.demo.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.core.ApiResponse;
import com.example.demo.model.dto.MessageDTO;
import com.example.demo.service.user.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    // 获取未读消息
    @GetMapping("/unread/{userId}")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getUnreadMessages(@PathVariable String userId) {
        try {
            List<MessageDTO> messages = messageService.getUnreadMessages(userId);
            return ResponseEntity.ok(ApiResponse.success("Successfully retrieved unread messages", messages));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "Failed to retrieve unread messages: " + e.getMessage()));
        }
    }

    // 标记消息为已读
    @PostMapping("/{messageId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable String messageId) {
        try {
            messageService.markAsRead(messageId);
            return ResponseEntity.ok(ApiResponse.success("Message marked as read", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "Failed to mark message as read: " + e.getMessage()));
        }
    }
} 
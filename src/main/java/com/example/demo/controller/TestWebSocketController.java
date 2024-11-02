/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-29 23:30:51
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2024-11-02 00:04:28
 */
package com.example.demo.controller.game;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;

@Controller
public class TestWebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(TestWebSocketController.class);

    @Operation(summary = "Test WebSocket", description = "Handles a test message and returns a response")
    @MessageMapping("/test")
    @SendTo("/topic/test")
    public String handleTest(String message) {
        logger.info("Received test message: {}", message);
        return "Server received: " + message;
    }
}
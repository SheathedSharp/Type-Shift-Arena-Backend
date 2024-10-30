/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-29 23:30:51
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-10-29 23:30:57
 */
package com.example.demo.controller.game;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TestWebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(TestWebSocketController.class);

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public String handleTest(String message) {
        logger.info("Received test message: {}", message);
        return "Server received: " + message;
    }
}
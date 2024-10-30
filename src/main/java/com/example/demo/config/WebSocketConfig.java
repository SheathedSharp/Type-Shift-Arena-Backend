/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-28 20:01:45
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-10-30 12:50:58
 */
package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.TaskScheduler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOrigins("http://localhost:5173")
            .withSockJS()
            .setWebSocketEnabled(true)
            .setDisconnectDelay(30 * 1000);
            
        // 同时添加一个不使用 SockJS 的端点，用于直接的 WebSocket 连接
        registry.addEndpoint("/ws")
            .setAllowedOrigins("http://localhost:5173");
            
        logger.info("WebSocket endpoint registered with SockJS support");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue");
        
        logger.info("Message broker configured");
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("ws-heartbeat-scheduler-");
        scheduler.initialize();
        return scheduler;
    }
}


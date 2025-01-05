/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-28 20:01:45
 */
package com.example.demo.config;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.example.demo.interceptor.UserActivityInterceptor;
import com.example.demo.security.JwtTokenUtil;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Value("${app.cors.allowed-origins:http://localhost:5173}")
    private String allowedOrigins;

    @Autowired
    private UserActivityInterceptor userActivityInterceptor; // 注入用户活动拦截器

    @Autowired
    private JwtTokenUtil jwtTokenUtil; // 注入 JwtTokenUtil

    @Autowired
    private UserRepository userRepository;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOrigins(allowedOrigins.split(","))
            .withSockJS()
            .setWebSocketEnabled(true)
            .setDisconnectDelay(30 * 1000);
            
        // 同时添加一个不使用 SockJS 的端点，用于直接的 WebSocket 连接
        registry.addEndpoint("/ws")
            .setAllowedOrigins(allowedOrigins.split(","));
            
        logger.info("WebSocket endpoint registered with allowed origins: {}", allowedOrigins);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue", "/user");
        
        logger.info("Message broker configured");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // 先尝试从 STOMP headers 获取 token
                    List<String> tokenHeader = accessor.getNativeHeader("token");
                    String token = null;
                    
                    if (tokenHeader != null && !tokenHeader.isEmpty()) {
                        token = tokenHeader.get(0);
                    } else {
                        // 如果 header 中没有，尝试从 URL 参数获取
                        String query = accessor.getNativeHeader("query-string") != null ? 
                            accessor.getNativeHeader("query-string").get(0) : "";
                        if (query != null && !query.isEmpty()) {
                            // 解析 URL 参数
                            String[] params = query.split("&");
                            for (String param : params) {
                                String[] keyValue = param.split("=");
                                if (keyValue.length == 2 && "token".equals(keyValue[0])) {
                                    token = keyValue[1];
                                    break;
                                }
                            }
                        }
                    }

                    // 验证 token 并设置用户信息
                    if (token != null) {
                        try {
                            String username = jwtTokenUtil.getUsernameFromToken(token);
                            if (username != null) {
                                // 通过用户名查找用户ID
                                User user = userRepository.findByUsername(username)
                                    .orElse(null);
                                if (user != null) {
                                    accessor.setUser(new Principal() {
                                        @Override
                                        public String getName() {
                                            return user.getId(); // 使用用户ID而不是用户名
                                        }
                                    });
                                    logger.info("WebSocket authentication successful for user: {} (ID: {})", username, user.getId());
                                } else {
                                    logger.warn("No user found with username: {}", username);
                                }
                            } else {
                                logger.warn("No username found in token");
                            }
                        } catch (Exception e) {
                            logger.error("Token validation failed", e);
                        }
                    } else {
                        logger.warn("No token found in connection request");
                    }
                }
                return message;
            }
        });
        
        // 添加现有的用户活动拦截器
        registration.interceptors(userActivityInterceptor);
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


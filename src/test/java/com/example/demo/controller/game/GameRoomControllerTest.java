/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-12-26 03:09:43
 */

package com.example.demo.controller.game;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.model.game.GameMessage;
import com.example.demo.model.game.GameRoom;
import com.example.demo.service.game.GameRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GameRoomControllerTest {

    @LocalServerPort
    private int port;

    private StompSession stompSession;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameRoomService gameRoomService;

    private GameMessage validRequest;
    private GameRoom mockRoom;

    @BeforeEach
    void setUp() throws InterruptedException, ExecutionException, TimeoutException {
        // 设置WebSocket客户端
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(
            List.of(new WebSocketTransport(new StandardWebSocketClient()))
        ));
        
        // 连接到WebSocket服务器
        stompSession = stompClient.connect(
            "ws://localhost:" + port + "/ws",
            new StompSessionHandlerAdapter() {}
        ).get(1, TimeUnit.SECONDS);

        validRequest = new GameMessage();
        validRequest.setPlayerId("test-player-123");
        validRequest.setLanguage("ENGLISH");
        validRequest.setCategory("DAILY_CHAT");
        validRequest.setDifficulty("EASY");

        mockRoom = new GameRoom();
        mockRoom.setId("test-room-123");
        mockRoom.setHostId("test-player-123");
        mockRoom.setLanguage(TextLanguage.ENGLISH);
        mockRoom.setCategory(TextCategory.DAILY_CHAT);
        mockRoom.setDifficulty("EASY");
        mockRoom.setTargetText("Test target text");
    }

    @AfterEach
    void tearDown() {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.disconnect();
        }
    }

    @Test
    @WithMockUser
    void createRoom_WithValidData_ShouldReturnSuccess() throws Exception {
        // 配置mock服务响应
        when(gameRoomService.createRoom(any(), any(), any(), any()))
            .thenReturn(mockRoom);

        // 执行测试请求
        mockMvc.perform(post("/api/rooms/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Room created successfully"))
                .andExpect(jsonPath("$.data.roomId").value("test-room-123"))
                .andExpect(jsonPath("$.data.hostId").value("test-player-123"))
                .andExpect(jsonPath("$.data.language").value("ENGLISH"))
                .andExpect(jsonPath("$.data.category").value("DAILY_CHAT"))
                .andExpect(jsonPath("$.data.difficulty").value("EASY"))
                .andExpect(jsonPath("$.data.playersCount").value(1));
    }

    @Test
    @WithMockUser
    void createRoom_WithInvalidLanguage_ShouldReturnBadRequest() throws Exception {
        // 设置无效的语言
        validRequest.setLanguage("INVALID_LANGUAGE");

        mockMvc.perform(post("/api/rooms/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Invalid parameters")));
    }

    @Test
    @WithMockUser
    void createRoom_WithMissingPlayerId_ShouldReturnBadRequest() throws Exception {
        // 设置请求缺少必要字段
        validRequest.setPlayerId(null);

        mockMvc.perform(post("/api/rooms/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void createRoom_WithDefaultValues_ShouldReturnSuccess() throws Exception {
        // 只设置必要的playerId，其他使用默认值
        GameMessage minimalRequest = new GameMessage();
        minimalRequest.setPlayerId("test-player-123");

        // 配置mock服务响应
        when(gameRoomService.createRoom(any(), any(), any(), any()))
            .thenReturn(mockRoom);

        mockMvc.perform(post("/api/rooms/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(minimalRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.language").value("ENGLISH"))
                .andExpect(jsonPath("$.data.category").value("DAILY_CHAT"))
                .andExpect(jsonPath("$.data.difficulty").value("EASY"));
    }
}
/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-12-26 03:09:43
 */
package com.example.demo.controller.game;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.model.game.GameMessage;
import com.example.demo.model.game.GameRoom;
import com.example.demo.service.game.GameRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GameRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameRoomService gameRoomService;

    @Test
    void createRoom_WithValidData_ShouldReturnSuccess() throws Exception {
        // 准备测试数据
        GameMessage request = new GameMessage();
        request.setPlayerId("test-player-123");
        request.setLanguage("ENGLISH");
        request.setCategory("DAILY_CHAT");
        request.setDifficulty("EASY");

        GameRoom mockRoom = new GameRoom();
        mockRoom.setId("test-room-123");
        
        // 模拟方法
        when(gameRoomService.createRoom(
            any(String.class),
            eq(TextLanguage.ENGLISH),
            eq(TextCategory.DAILY_CHAT),
            eq("EASY")
        )).thenReturn(mockRoom);

        // 执行测试
        mockMvc.perform(post("/api/game-rooms/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void createRoom_WithInvalidLanguage_ShouldReturnBadRequest() throws Exception {
        GameMessage request = new GameMessage();
        request.setPlayerId("test-player-123");
        request.setLanguage("INVALID");
        request.setCategory("DAILY_CHAT");
        request.setDifficulty("EASY");

        mockMvc.perform(post("/api/game-rooms/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

package com.example.demo.service;

import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.entity.enums.TextCategory;
import com.example.demo.model.game.GameMessage;
import com.example.demo.model.game.GameRoom;
import com.example.demo.model.game.GameStatus;
import com.example.demo.model.game.MatchRequest;
import com.example.demo.service.game.GameRoomService;
import com.example.demo.service.game.MatchmakingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MatchmakingServiceTest {

    @Mock
    private GameRoomService gameRoomService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private MatchmakingService matchmakingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddToQueueAndFindMatch() {
        // Arrange
        long timestamp = System.currentTimeMillis();
        MatchRequest player1 = new MatchRequest("player1", "Player 1", "avatar1", TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY", timestamp);
        MatchRequest player2 = new MatchRequest("player2", "Player 2", "avatar2", TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY", timestamp);

        // Act
        matchmakingService.addToQueue(player1);
        matchmakingService.addToQueue(player2);

        // Assert
        String queueKey = matchmakingService.generateQueueKey(TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY");
        Queue<MatchRequest> queue = matchmakingService.getMatchmakingQueues().get(queueKey);
        assertNotNull(queue);
        assertEquals(0, queue.size()); // 队列应为空，因为匹配成功
        verify(messagingTemplate, times(2)).convertAndSend(anyString(), any(GameMessage.class));
    }

    @Test
    void shouldAddToQueueAndWaitForMatch() {
        // Arrange
        long timestamp = System.currentTimeMillis();
        MatchRequest player1 = new MatchRequest("player1", "Player 1", "avatar1", TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY", timestamp);

        // Act
        matchmakingService.addToQueue(player1);

        // Assert
        String queueKey = matchmakingService.generateQueueKey(TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY");
        Queue<MatchRequest> queue = matchmakingService.getMatchmakingQueues().get(queueKey);
        assertNotNull(queue);
        assertEquals(1, queue.size()); // 队列中应有一个玩家等待匹配
        verify(messagingTemplate, never()).convertAndSend(anyString(), any(GameMessage.class));
    }

    @Test
    void shouldRemoveFromQueue() {
        // Arrange
        long timestamp = System.currentTimeMillis();
        MatchRequest player1 = new MatchRequest("player1", "Player 1", "avatar1", TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY", timestamp);
        matchmakingService.addToQueue(player1);

        // Act
        matchmakingService.removeFromQueue(player1);

        // Assert
        String queueKey = matchmakingService.generateQueueKey(TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY");
        Queue<MatchRequest> queue = matchmakingService.getMatchmakingQueues().get(queueKey);
        assertNull(queue); // 队列应为空，并从 map 中移除
        verify(messagingTemplate, times(1)).convertAndSend(eq("/queue/matchmaking/player1"), any(GameMessage.class));
    }

    @Test
    void shouldCreateMatchedRoom() {
        // Arrange
        long timestamp = System.currentTimeMillis();
        MatchRequest player1 = new MatchRequest("player1", "Player 1", "avatar1", TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY", timestamp);
        MatchRequest player2 = new MatchRequest("player2", "Player 2", "avatar2", TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY", timestamp);

        GameRoom mockRoom = new GameRoom();
        mockRoom.setRoomId(UUID.randomUUID().toString());
        when(gameRoomService.createRoom(anyString(), eq(TextLanguage.ENGLISH), eq(TextCategory.DAILY_CHAT), eq("EASY"))).thenReturn(mockRoom);

        // Act
        matchmakingService.createMatchedRoom(player1, player2);

        // Assert
        verify(gameRoomService, times(1)).createRoom(anyString(), eq(TextLanguage.ENGLISH), eq(TextCategory.DAILY_CHAT), eq("EASY"));
        verify(gameRoomService, times(1)).addPlayer(mockRoom, "player1", "Player 1");
        verify(gameRoomService, times(1)).addPlayer(mockRoom, "player2", "Player 2");
        verify(messagingTemplate, times(2)).convertAndSend(anyString(), any(GameMessage.class));
    }

    @Test
    void shouldGenerateQueueKey() {
        // Arrange
        TextLanguage language = TextLanguage.ENGLISH;
        TextCategory category = TextCategory.DAILY_CHAT;
        String difficulty = "EASY";

        // Act
        String queueKey = matchmakingService.generateQueueKey(language, category, difficulty);

        // Assert
        assertEquals("ENGLISH_DAILY_CHAT_EASY", queueKey);
    }

    @Test
    void shouldFindMatchInQueue() {
        // Arrange
        long timestamp = System.currentTimeMillis();
        MatchRequest player1 = new MatchRequest("player1", "Player 1", "avatar1", TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY", timestamp);
        MatchRequest player2 = new MatchRequest("player2", "Player 2", "avatar2", TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY", timestamp);
        Queue<MatchRequest> queue = new ConcurrentLinkedQueue<>();
        queue.offer(player1);

        // Act
        MatchRequest match = matchmakingService.findMatch(queue, player2);

        // Assert
        assertNotNull(match);
        assertEquals("player1", match.getPlayerId());
        assertTrue(queue.isEmpty());
    }

    @Test
    void shouldNotFindMatchInEmptyQueue() {
        // Arrange
        long timestamp = System.currentTimeMillis();
        MatchRequest player2 = new MatchRequest("player2", "Player 2", "avatar2", TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY", timestamp);
        Queue<MatchRequest> queue = new ConcurrentLinkedQueue<>();

        // Act
        MatchRequest match = matchmakingService.findMatch(queue, player2);

        // Assert
        assertNull(match);
        assertTrue(queue.isEmpty());
    }
}

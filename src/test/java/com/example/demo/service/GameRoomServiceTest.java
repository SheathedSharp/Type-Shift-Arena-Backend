package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.model.game.GameMessage;
import com.example.demo.model.game.GameRoom;
import com.example.demo.model.game.GameStatus;
import com.example.demo.service.game.GameRoomService;
import com.example.demo.service.game.GameTextService;
import com.example.demo.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GameRoomServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private GameTextService gameTextService;

    @Mock
    private UserService userService;

    @InjectMocks
    private GameRoomService gameRoomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateRoomWithParameters() {
        // Arrange
        String roomId = "room1";
        TextLanguage language = TextLanguage.ENGLISH;
        TextCategory category = TextCategory.DAILY_CHAT;
        String difficulty = "EASY";
        String targetText = "Sample text";

        when(gameTextService.getRandomText(language, category, difficulty)).thenReturn(targetText);

        // Act
        GameRoom room = gameRoomService.createRoom(roomId, language, category, difficulty);

        // Assert
        assertNotNull(room);
        assertEquals(roomId, room.getRoomId());
        assertEquals(language, room.getLanguage());
        assertEquals(category, room.getCategory());
        assertEquals(difficulty, room.getDifficulty());
        assertEquals(targetText, room.getTargetText());
        assertEquals(GameStatus.WAITING, room.getStatus());
        verify(gameTextService, times(1)).getRandomText(language, category, difficulty);
    }

    @Test
    void shouldCreateRoomWithDefaultParameters() {
        // Arrange
        String roomId = "room1";
        String targetText = "Sample text";

        when(gameTextService.getRandomText(TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY")).thenReturn(targetText);

        // Act
        GameRoom room = gameRoomService.createRoom(roomId);

        // Assert
        assertNotNull(room);
        assertEquals(roomId, room.getRoomId());
        assertEquals(TextLanguage.ENGLISH, room.getLanguage());
        assertEquals(TextCategory.DAILY_CHAT, room.getCategory());
        assertEquals("EASY", room.getDifficulty());
        assertEquals(targetText, room.getTargetText());
        assertEquals(GameStatus.WAITING, room.getStatus());
        verify(gameTextService, times(1)).getRandomText(TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY");
    }

    @Test
    void shouldJoinRoomAndCreateIfNotExists() {
        // Arrange
        String roomId = "room1";
        String playerId = "player1";
        String playerName = "John";
        TextLanguage language = TextLanguage.ENGLISH;
        TextCategory category = TextCategory.DAILY_CHAT;
        String difficulty = "EASY";
        String targetText = "Sample text";

        when(gameTextService.getRandomText(language, category, difficulty)).thenReturn(targetText);

        // Act
        GameRoom room = gameRoomService.joinRoom(roomId, playerId, playerName, language, category, difficulty);

        // Assert
        assertNotNull(room);
        assertEquals(roomId, room.getRoomId());
        assertTrue(room.getPlayersId().contains(playerId));
        assertTrue(room.getPlayersName().contains(playerName));
        assertEquals(GameStatus.WAITING, room.getStatus());
        verify(gameTextService, times(1)).getRandomText(language, category, difficulty);
    }

    @Test
    void shouldJoinRoomWithDefaultParameters() {
        // Arrange
        String roomId = "room1";
        String playerId = "player1";
        String playerName = "John";
        String targetText = "Sample text";

        when(gameTextService.getRandomText(TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY")).thenReturn(targetText);

        // Act
        GameRoom room = gameRoomService.joinRoom(roomId, playerId, playerName);

        // Assert
        assertNotNull(room);
        assertEquals(roomId, room.getRoomId());
        assertTrue(room.getPlayersId().contains(playerId));
        assertTrue(room.getPlayersName().contains(playerName));
        assertEquals(GameStatus.WAITING, room.getStatus());
        verify(gameTextService, times(1)).getRandomText(TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY");
    }

    @Test
    void shouldLeaveRoomAndRemoveIfEmpty() {
        // Arrange
        String roomId = "room1";
        String playerId = "player1";
        String playerName = "John";
        GameRoom room = new GameRoom(roomId);
        room.getPlayersId().add(playerId);
        room.getPlayersName().add(playerName);

        Map<String, GameRoom> rooms = new ConcurrentHashMap<>();
        rooms.put(roomId, room);
        gameRoomService = new GameRoomService(messagingTemplate, gameTextService, userService) {
            {
                this.rooms = rooms;
            }
        };

        // Act
        gameRoomService.leaveRoom(roomId, playerId, playerName);

        // Assert
        assertFalse(rooms.containsKey(roomId));
    }

    @Test
    void shouldUpdateGameStatus() {
        // Arrange
        String roomId = "room1";
        GameRoom room = new GameRoom(roomId);
        Map<String, GameRoom> rooms = new ConcurrentHashMap<>();
        rooms.put(roomId, room);
        gameRoomService = new GameRoomService(messagingTemplate, gameTextService, userService) {
            {
                this.rooms = rooms;
            }
        };

        // Act
        gameRoomService.updateGameStatus(roomId, GameStatus.PLAYING);

        // Assert
        assertEquals(GameStatus.PLAYING, room.getStatus());
        assertTrue(room.getStartTime() > 0);
    }

    @Test
    void shouldGetRoomInfo() {
        // Arrange
        String roomId = "room1";
        String playerId = "player1";
        String playerName = "John";
        String opponentId = "player2";
        String opponentName = "Jane";
        String playerAvatar = "avatar1";
        String opponentAvatar = "avatar2";
        GameRoom room = new GameRoom(roomId);
        room.getPlayersId().add(playerId);
        room.getPlayersId().add(opponentId);
        room.getPlayersName().add(playerName);
        room.getPlayersName().add(opponentName);
        room.setLanguage(TextLanguage.ENGLISH);
        room.setCategory(TextCategory.DAILY_CHAT);
        room.setDifficulty("EASY");
        room.setTargetText("Sample text");

        Map<String, GameRoom> rooms = new ConcurrentHashMap<>();
        rooms.put(roomId, room);
        gameRoomService = new GameRoomService(messagingTemplate, gameTextService, userService) {
            {
                this.rooms = rooms;
            }
        };

        when(userService.getUserById(Long.parseLong(playerId))).thenReturn(Optional.of(new User() {{
            setImgSrc(playerAvatar);
        }}));
        when(userService.getUserById(Long.parseLong(opponentId))).thenReturn(Optional.of(new User() {{
            setImgSrc(opponentAvatar);
        }}));

        // Act
        GameMessage roomInfo = gameRoomService.getRoomInfo(roomId, playerId, playerName);

        // Assert
        assertNotNull(roomInfo);
        assertEquals(roomId, roomInfo.getRoomId());
        assertEquals(playerId, roomInfo.getPlayerId());
        assertEquals(playerName, roomInfo.getPlayerName());
        assertEquals(playerAvatar, roomInfo.getPlayerAvatar());
        assertEquals(opponentId, roomInfo.getOpponentId());
        assertEquals(opponentName, roomInfo.getOpponentName());
        assertEquals(opponentAvatar, roomInfo.getOpponentAvatar());
        assertEquals(TextLanguage.ENGLISH.toString(), roomInfo.getLanguage());
        assertEquals(TextCategory.DAILY_CHAT.toString(), roomInfo.getCategory());
        assertEquals("EASY", roomInfo.getDifficulty());
        assertEquals(GameStatus.WAITING.toString(), roomInfo.getRoomStatus());
        assertEquals(2, roomInfo.getPlayersCount());
        assertEquals("Sample text", roomInfo.getTargetText());
    }

    @Test
    void shouldPlayerReady() {
        // Arrange
        String roomId = "room1";
        String playerId = "player1";
        String playerName = "John";
        String opponentId = "player2";
        GameRoom room = new GameRoom(roomId);
        room.getPlayersId().add(playerId);
        room.getPlayersId().add(opponentId);

        Map<String, GameRoom> rooms = new ConcurrentHashMap<>();
        rooms.put(roomId, room);
        gameRoomService = new GameRoomService(messagingTemplate, gameTextService, userService) {
            {
                this.rooms = rooms;
            }
        };

        // Act
        gameRoomService.playerReady(roomId, playerId, playerName, true);

        // Assert
        assertEquals(GameStatus.READY, room.getStatus());
        verify(messagingTemplate, times(1)).convertAndSend(anyString(), any(GameMessage.class));
    }
}

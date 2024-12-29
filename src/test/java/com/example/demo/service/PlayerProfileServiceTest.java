package com.example.demo.service;

import com.example.demo.entity.PlayerProfile;
import com.example.demo.repository.PlayerProfileRepository;
import com.example.demo.service.user.PlayerProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlayerProfileServiceTest {

    @Mock
    private PlayerProfileRepository playerProfileRepository;

    @InjectMocks
    private PlayerProfileService playerProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUpdatePlayerStatsForVictory() {
        // Arrange
        Long playerId = 1L;
        PlayerProfile profile = new PlayerProfile();
        profile.setId(playerId);
        profile.setMatchesPlayed(5);
        profile.setWin(3);
        profile.setSpeed(50.0);
        profile.setWinRate(60.0);
        profile.setAccuracyRate(80.0);
        profile.setRankScore(1200);
        profile.setUserLevel("不屈白银");

        when(playerProfileRepository.findById(playerId)).thenReturn(Optional.of(profile));
        when(playerProfileRepository.save(any(PlayerProfile.class))).thenReturn(profile);

        // Act
        PlayerProfile updatedProfile = playerProfileService.updatePlayerStats(playerId, 60.0, true, 85.0);

        // Assert
        assertNotNull(updatedProfile);
        assertEquals(6, updatedProfile.getMatchesPlayed()); // 比赛场次 +1
        assertEquals(4, updatedProfile.getWin()); // 胜场 +1
        assertEquals(55.0, updatedProfile.getSpeed()); // 平均速度更新
        assertEquals(66.67, updatedProfile.getWinRate(), 0.01); // 胜率更新
        assertEquals(80.83, updatedProfile.getAccuracyRate(), 0.01); // 准确率更新
        assertEquals(1300, updatedProfile.getRankScore()); // 段位积分 +100
        assertEquals("不屈白银", updatedProfile.getUserLevel()); // 等级不变
        verify(playerProfileRepository, times(1)).findById(playerId);
        verify(playerProfileRepository, times(1)).save(profile);
    }

    @Test
    void shouldUpdatePlayerStatsForDefeat() {
        // Arrange
        Long playerId = 1L;
        PlayerProfile profile = new PlayerProfile();
        profile.setId(playerId);
        profile.setMatchesPlayed(5);
        profile.setWin(3);
        profile.setSpeed(50.0);
        profile.setWinRate(60.0);
        profile.setAccuracyRate(80.0);
        profile.setRankScore(1200);
        profile.setUserLevel("不屈白银");

        when(playerProfileRepository.findById(playerId)).thenReturn(Optional.of(profile));
        when(playerProfileRepository.save(any(PlayerProfile.class))).thenReturn(profile);

        // Act
        PlayerProfile updatedProfile = playerProfileService.updatePlayerStats(playerId, 60.0, false, 85.0);

        // Assert
        assertNotNull(updatedProfile);
        assertEquals(6, updatedProfile.getMatchesPlayed()); // 比赛场次 +1
        assertEquals(3, updatedProfile.getWin()); // 胜场不变
        assertEquals(55.0, updatedProfile.getSpeed()); // 平均速度更新
        assertEquals(50.0, updatedProfile.getWinRate(), 0.01); // 胜率更新
        assertEquals(80.83, updatedProfile.getAccuracyRate(), 0.01); // 准确率更新
        assertEquals(1150, updatedProfile.getRankScore()); // 段位积分 -50
        assertEquals("不屈白银", updatedProfile.getUserLevel()); // 等级不变
        verify(playerProfileRepository, times(1)).findById(playerId);
        verify(playerProfileRepository, times(1)).save(profile);
    }

    @Test
    void shouldUpdateUserLevelToHighest() {
        // Arrange
        Long playerId = 1L;
        PlayerProfile profile = new PlayerProfile();
        profile.setId(playerId);
        profile.setMatchesPlayed(5);
        profile.setWin(3);
        profile.setSpeed(50.0);
        profile.setWinRate(60.0);
        profile.setAccuracyRate(80.0);
        profile.setRankScore(4000); // 当前段位积分
        profile.setUserLevel("超凡大师");

        when(playerProfileRepository.findById(playerId)).thenReturn(Optional.of(profile));
        when(playerProfileRepository.save(any(PlayerProfile.class))).thenReturn(profile);

        // Act
        PlayerProfile updatedProfile = playerProfileService.updatePlayerStats(playerId, 60.0, true, 85.0);

        // Assert
        assertNotNull(updatedProfile);
        assertEquals(4100, updatedProfile.getRankScore()); // 段位积分 +100
        assertEquals("最强王者", updatedProfile.getUserLevel()); // 等级更新
        verify(playerProfileRepository, times(1)).findById(playerId);
        verify(playerProfileRepository, times(1)).save(profile);
    }

    @Test
    void shouldThrowExceptionWhenPlayerProfileNotFound() {
        // Arrange
        Long playerId = 1L;
        when(playerProfileRepository.findById(playerId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            playerProfileService.updatePlayerStats(playerId, 60.0, true, 85.0);
        });
        assertEquals("Player profile not found", exception.getMessage());
        verify(playerProfileRepository, times(1)).findById(playerId);
        verify(playerProfileRepository, never()).save(any(PlayerProfile.class));
    }

    @Test
    void shouldGetPlayerProfileById() {
        // Arrange
        Long playerId = 1L;
        PlayerProfile profile = new PlayerProfile();
        profile.setId(playerId);
        profile.setUserLevel("不屈白银");

        when(playerProfileRepository.findById(playerId)).thenReturn(Optional.of(profile));

        // Act
        PlayerProfile result = playerProfileService.getPlayerProfileById(playerId);

        // Assert
        assertNotNull(result);
        assertEquals(playerId, result.getId());
        assertEquals("不屈白银", result.getUserLevel());
        verify(playerProfileRepository, times(1)).findById(playerId);
    }

    @Test
    void shouldReturnNullWhenPlayerProfileNotFound() {
        // Arrange
        Long playerId = 1L;
        when(playerProfileRepository.findById(playerId)).thenReturn(Optional.empty());

        // Act
        PlayerProfile result = playerProfileService.getPlayerProfileById(playerId);

        // Assert
        assertNull(result);
        verify(playerProfileRepository, times(1)).findById(playerId);
    }
}


/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-12-30 18:55:51
 */
package com.example.demo.service.game;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.entity.GameMatch;
import com.example.demo.entity.PlayerProfile;
import com.example.demo.entity.User;
import com.example.demo.model.dto.game.GameMatchDTO;
import com.example.demo.model.dto.game.MatchResultDTO;
import com.example.demo.repository.GameMatchRepository;
import com.example.demo.service.user.PlayerProfileService;
import com.example.demo.service.user.UserService;

@Service
public class GameMatchService {
    @Autowired
    private GameMatchRepository gameMatchRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PlayerProfileService playerProfileService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private RankingService rankingService;

    public GameMatch recordMatch(GameMatchDTO matchDTO) {
        GameMatch match = new GameMatch();
        
        // 设置基本信息
        match.setRoomId(matchDTO.getRoomId());
        match.setLanguage(matchDTO.getLanguage());
        match.setCategory(matchDTO.getCategory());
        match.setDifficulty(matchDTO.getDifficulty());
        match.setStartTime(matchDTO.getStartTime());
        match.setEndTime(matchDTO.getEndTime());
        match.setRanked(matchDTO.isRanked());
        match.setTargetText(matchDTO.getTargetText());
        
        // 设置玩家信息
        User player1 = userService.getUserById(Long.parseLong(matchDTO.getPlayer1Id()))
            .orElseThrow(() -> new RuntimeException("Player 1 not found"));
        User player2 = userService.getUserById(Long.parseLong(matchDTO.getPlayer2Id()))
            .orElseThrow(() -> new RuntimeException("Player 2 not found"));
        
        match.setPlayer1(player1);
        match.setPlayer2(player2);
        
        // 设置胜者
        if (matchDTO.getWinnerId() != null) {
            User winner = userService.getUserById(Long.parseLong(matchDTO.getWinnerId()))
                .orElseThrow(() -> new RuntimeException("Winner not found"));
            match.setWinner(winner);
        }
        
        // 设置表现数据
        match.setPlayer1Wpm(matchDTO.getPlayer1Wpm());
        match.setPlayer2Wpm(matchDTO.getPlayer2Wpm());
        match.setPlayer1Accuracy(matchDTO.getPlayer1Accuracy());
        match.setPlayer2Accuracy(matchDTO.getPlayer2Accuracy());
        
        // 创建比赛结果DTO
        MatchResultDTO resultDTO = new MatchResultDTO();
        resultDTO.setType("MATCH_RESULT");
        
        // 如果是排位赛，计算积分变化
        PlayerProfile player1Profile = playerProfileService.getPlayerProfileById(        // 获取玩家档案
            Long.parseLong(matchDTO.getPlayer1Id()));
        PlayerProfile player2Profile = playerProfileService.getPlayerProfileById(        // 获取玩家档案
            Long.parseLong(matchDTO.getPlayer2Id()));

        int player1ScoreChange = 0;
        int player2ScoreChange = 0;
        int player1OldScore = player1Profile.getRankScore();
        int player2OldScore = player2Profile.getRankScore();

        if (matchDTO.isRanked()) {
            // 计算积分变化
            player1ScoreChange = rankingService.calculateRankScoreChange(
                player1Profile,
                matchDTO.getPlayer1Wpm(),
                matchDTO.getPlayer1Accuracy(),
                matchDTO.getWinnerId().equals(matchDTO.getPlayer1Id())
            );
            
            player2ScoreChange = rankingService.calculateRankScoreChange(
                player2Profile,
                matchDTO.getPlayer2Wpm(),
                matchDTO.getPlayer2Accuracy(),
                matchDTO.getWinnerId().equals(matchDTO.getPlayer2Id())
            );
        }

        // 更新玩家档案
        updatePlayerProfiles(matchDTO);
        
        // 保存比赛记录
        GameMatch savedMatch = gameMatchRepository.save(match);
        
        // 发送结果给玩家1
        MatchResultDTO player1Result = new MatchResultDTO();
        player1Result.setType("GAME_RESULT");
        player1Result.setMatch(savedMatch);
        player1Result.setScoreChange(player1ScoreChange);
        player1Result.setOldScore(player1OldScore);
        sendMatchResult(matchDTO.getPlayer1Id(), player1Result);
        
        // 发送结果给玩家2
        MatchResultDTO player2Result = new MatchResultDTO();
        player2Result.setType("GAME_RESULT");
        player2Result.setMatch(savedMatch);
        player2Result.setScoreChange(player2ScoreChange);
        player2Result.setOldScore(player2OldScore);
        sendMatchResult(matchDTO.getPlayer2Id(), player2Result);
        
        return savedMatch;
    }
    
    private void sendMatchResult(String playerId, MatchResultDTO resultDTO) {
        messagingTemplate.convertAndSend(
            "/queue/room/" + playerId + "/info",
            resultDTO
        );
    }

    // 更新游戏玩家档案
    private void updatePlayerProfiles(GameMatchDTO matchDTO) {
        // 更新玩家1的档案
        playerProfileService.updatePlayerStats(
            Long.parseLong(matchDTO.getPlayer1Id()),
            matchDTO.getPlayer1Wpm(),
            matchDTO.getPlayer1Accuracy(),
            matchDTO.getWinnerId().equals(matchDTO.getPlayer1Id()),
            matchDTO.isRanked()
        );
        
        // 更新玩家2的档案
        playerProfileService.updatePlayerStats(
            Long.parseLong(matchDTO.getPlayer2Id()),
            matchDTO.getPlayer2Wpm(),
            matchDTO.getPlayer2Accuracy(),
            matchDTO.getWinnerId().equals(matchDTO.getPlayer2Id()),
            matchDTO.isRanked()
        );
    }

    // 通过ID查询比赛记录
    public Optional<GameMatch> getMatchById(String id) {
        return gameMatchRepository.findById(id);
    }

    public List<GameMatch> getPlayerMatches(Long playerId) {
        // 查询该玩家作为player1或player2的所有比赛
        return gameMatchRepository.findByPlayer1IdOrPlayer2Id(playerId, playerId);
    }

    // 获取玩家的排位赛记录
    public List<GameMatch> getPlayerRankedMatches(Long playerId) {
        return gameMatchRepository.findByPlayer1IdOrPlayer2Id(playerId, playerId)
            .stream()
            .filter(GameMatch::isRanked)
            .collect(Collectors.toList());
    }

    // 获取玩家的非排位赛记录
    public List<GameMatch> getPlayerCasualMatches(Long playerId) {
        return gameMatchRepository.findByPlayer1IdOrPlayer2Id(playerId, playerId)
            .stream()
            .filter(match -> !match.isRanked())
            .collect(Collectors.toList());
    }
}
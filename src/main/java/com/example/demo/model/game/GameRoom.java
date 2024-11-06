/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-29 22:45:53
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-06 21:44:36
 */
package com.example.demo.model.game;

import java.util.HashSet;
import java.util.Set;

public class GameRoom {
    private String id;
    private String targetText;
    private Set<String> playersId;
    private Set<String> playersName;
    private GameStatus status;
    private Long startTime;

    public GameRoom(String id) {
        this.id = id;
        this.playersId = new HashSet<>();
        this.playersName = new HashSet<>();
        this.status = GameStatus.WAITING;
        this.targetText = "The quick brown fox jumps over the lazy dog."; // 默认文本
        this.startTime = System.currentTimeMillis();
    }

    // Getters and setters
    public String getId() { return id; }
    public String getTargetText() { return targetText; }
    public Set<String> getPlayersId() { return playersId; }
    public Set<String> getPlayersName() { return playersName; }
    public GameStatus getStatus() { return status; }
    public Long getStartTime() { return startTime; }

    public void setStatus(GameStatus status) {
        this.status = status;
        if (status == GameStatus.PLAYING) {
            this.startTime = System.currentTimeMillis();
        }
    }

    public boolean addPlayer(String playerId, String playerName) {
        if (!isFull()) {
            playersId.add(playerId);
            playersName.add(playerName);
            return true;
        }
        return false;
    }

    public boolean removePlayer(String playerId, String playerName) {
        playersId.remove(playerId);
        playersName.remove(playerName);
        return playersId.isEmpty();
    }

    public boolean isFull() {
        return playersId.size() >= 2;
    }

    public boolean isEmpty() {
        return playersId.isEmpty();
    }

    public boolean isHost(String playerId) {
        return !playersId.isEmpty() && playersId.iterator().next().equals(playerId);
    }

    public boolean hasPlayer(String playerId) {
        return playersId.contains(playerId);
    }
}
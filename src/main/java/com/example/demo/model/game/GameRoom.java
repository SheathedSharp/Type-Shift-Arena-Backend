/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-29 22:45:53
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-10-30 13:58:41
 */
package com.example.demo.model.game;

import java.util.HashSet;
import java.util.Set;

public class GameRoom {
    private String id;
    private String targetText;
    private Set<String> players;
    private GameStatus status;
    private Long startTime;

    public GameRoom(String id) {
        this.id = id;
        this.players = new HashSet<>();
        this.status = GameStatus.WAITING;
        this.targetText = "The quick brown fox jumps over the lazy dog."; // 默认文本
    }

    // Getters and setters
    public String getId() { return id; }
    public String getTargetText() { return targetText; }
    public Set<String> getPlayers() { return players; }
    public GameStatus getStatus() { return status; }
    public Long getStartTime() { return startTime; }

    public void setStatus(GameStatus status) {
        this.status = status;
        if (status == GameStatus.PLAYING) {
            this.startTime = System.currentTimeMillis();
        }
    }

    public boolean addPlayer(String playerId) {
        if (!isFull()) {
            return players.add(playerId);
        }
        return false;
    }

    public boolean removePlayer(String playerId) {
        return players.remove(playerId);
    }

    public boolean isFull() {
        return players.size() >= 2;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean isHost(String playerId) {
        return !players.isEmpty() && players.iterator().next().equals(playerId);
    }

    public boolean hasPlayer(String playerId) {
        return players.contains(playerId);
    }
}
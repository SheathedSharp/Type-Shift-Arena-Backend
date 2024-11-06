/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-28 20:03:08
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-10-28 20:03:23
 */
package com.example.demo.model.game;

public class GameProgress {
    private String playerId;
    private double percentage;

    // Getters and Setters
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}

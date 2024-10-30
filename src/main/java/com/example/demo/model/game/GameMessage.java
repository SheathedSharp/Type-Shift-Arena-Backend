package com.example.demo.model.game;

public class GameMessage {
    private String type;
    private String playerId;
    private Double percentage;
    private String timestamp;
    private String message;

    // 构造函数
    public GameMessage() {}

    public GameMessage(String type, String playerId) {
        this.type = type;
        this.playerId = playerId;
    }

    // Getters and setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
    
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

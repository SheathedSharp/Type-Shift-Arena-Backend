/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-06 18:19:04
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-15 21:39:22
 */
package com.example.demo.model.game;

import com.example.demo.config.CustomTimestampDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameMessage {
    private String type;
    private String playerId;
    private String playerName;
    private String playerAvatar;
    private String opponentId;
    private String opponentName;
    private String opponentAvatar;
    private Set<String> playersId;
    private Set<String> playersName;
    private Double percentage;
    private String message;
    private Boolean isHost;  // 添加是否为房主的标识
    private String roomId;     // 添加房间ID属性        
    private String roomStatus;    // 房间状态
    private String targetText;    // 目标文本
    private int playersCount;     // 玩家数量
    private Long startTime;       // 游戏开始时间
    private String language;    // 对应 TextLanguage
    private String category;    // 对应 TextCategory 
    private String difficulty; // 难度级别
    private Boolean isReady;  // 准备状态字段
    @JsonDeserialize(using = CustomTimestampDeserializer.class) // 使用自定义的时间戳反序列化器将时间戳转换为Long类型
    private Long timestamp;
}
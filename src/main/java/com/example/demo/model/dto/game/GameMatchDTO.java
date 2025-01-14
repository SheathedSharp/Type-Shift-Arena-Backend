/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-30 18:55:04
 */
package com.example.demo.model.dto.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameMatchDTO {
    private String roomId;
    private String player1Id;
    private String player2Id;
    private String winnerId;
    private String language;
    private String category;
    private String difficulty;
    private double player1Wpm;
    private double player2Wpm;
    private double player1Accuracy;
    private double player2Accuracy;
    private Long startTime;
    private Long endTime;
    private boolean isRanked;
    private String targetText;
}
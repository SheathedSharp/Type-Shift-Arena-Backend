/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-12-30 18:52:26
 */

package com.example.demo.entity;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "game_matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameMatch {
    @Id
    @Column(length = 36) // UUID字符串长度为36
    private String id;  // 改为String类型存储UUID

    @Column(name = "room_id", nullable = false)
    private String roomId;

    @ManyToOne
    @JoinColumn(name = "player1_id", nullable = false)
    @JsonIgnoreProperties({"friends", "password"})
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player2_id", nullable = false)
    @JsonIgnoreProperties({"friends", "password"})
    private User player2;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    @JsonIgnoreProperties({"friends", "password"})
    private User winner;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "difficulty", nullable = false)
    private String difficulty;

    @Column(name = "player1_wpm")
    private double player1Wpm;

    @Column(name = "player2_wpm")
    private double player2Wpm;

    @Column(name = "player1_accuracy")
    private double player1Accuracy;

    @Column(name = "player2_accuracy")
    private double player2Accuracy;

    @Column(name = "start_time", nullable = false)
    private Long startTime;

    @Column(name = "end_time", nullable = false)
    private Long endTime;

    @Column(name = "is_ranked", nullable = false)
    private boolean isRanked;

    @Column(name = "target_text", columnDefinition = "TEXT")
    private String targetText;

    // 在创建实体时生成UUID
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
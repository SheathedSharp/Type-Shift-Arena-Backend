/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-29 22:45:53
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-10 12:27:39
 */
package com.example.demo.model.game;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRoom {
    private String id;
    private String targetText = "The quick brown fox jumps over the lazy dog."; // 默认文本
    private Set<String> playersId = new HashSet<>();
    private Set<String> playersName = new HashSet<>();
    private GameStatus status = GameStatus.WAITING;
    private Long startTime = System.currentTimeMillis();

    public GameRoom(String id) {
        this.id = id;
    }
}
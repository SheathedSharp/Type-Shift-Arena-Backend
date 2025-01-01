/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-29 22:45:53
 */
package com.example.demo.model.game;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRoom {
    private String id;
    private String targetText = "";
    private Set<String> playersId = new HashSet<>();
    private Set<String> playersName = new HashSet<>();
    private GameStatus status = GameStatus.WAITING;
    private Long startTime = System.currentTimeMillis();
    private TextLanguage language;
    private TextCategory category;
    private String difficulty;
    private String hostId;
    private boolean isRanked = false; // 默认为非排位房间(自定义房间)

    public GameRoom(String id) {
        this.id = id;
    }
}
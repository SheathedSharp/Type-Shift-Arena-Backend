/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-28 20:03:08
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-10 12:26:55
 */
package com.example.demo.model.game;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameProgress {
    private String type;
    private String playerId;
    private double percentage;
    private Stats stats;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stats {
        private double wpm;
        private double accuracy;
        private int errorCount;
        private String username;
    }
}

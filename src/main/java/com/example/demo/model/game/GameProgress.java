/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-28 20:03:08
 */
package com.example.demo.model.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameProgress {
    private String type;
    private String playerId;
    private Stats stats;
    private long timestamp;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stats {
        private double wpm;
        private double accuracy;
        private int errorCount;
        private double progress;
        private String username;
    }
}

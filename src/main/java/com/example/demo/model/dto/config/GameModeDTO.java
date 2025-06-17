/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 10:00:00
 */
package com.example.demo.model.dto.config;

import java.util.List;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameModeDTO extends GameConfigDTO {
    private Integer minPlayers; // 最小玩家数
    private Integer maxPlayers; // 最大玩家数
    private Integer defaultMaxPlayers; // 默认最大玩家数
    private List<Integer> playerOptions; // 可选人数选项列表
    private Integer duration; // 游戏持续盘数
    private LocalTime availableTimeStart; // 开放时间段-开始时间
    private LocalTime availableTimeEnd; // 开放时间段-结束时间
    private List<String> supportedLanguageNames; // 支持的语言名称列表
} 
/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 10:00:00
 */
package com.example.demo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏模式工具类
 */
public class GameModeUtil {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 解析玩家选项JSON数组
     * @param playerOptionsJson JSON字符串，如"[2]"或"[2,4,6,8]"
     * @return 玩家数量选项列表
     */
    public static List<Integer> parsePlayerOptions(String playerOptionsJson) {
        if (playerOptionsJson == null || playerOptionsJson.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            return objectMapper.readValue(playerOptionsJson, new TypeReference<List<Integer>>() {});
        } catch (Exception e) {
            // 如果解析失败，返回空列表
            return new ArrayList<>();
        }
    }
    
    /**
     * 将玩家选项列表转换为JSON字符串
     * @param playerOptions 玩家数量选项列表
     * @return JSON字符串
     */
    public static String toPlayerOptionsJson(List<Integer> playerOptions) {
        if (playerOptions == null || playerOptions.isEmpty()) {
            return "[]";
        }
        
        try {
            return objectMapper.writeValueAsString(playerOptions);
        } catch (Exception e) {
            return "[]";
        }
    }
    
    /**
     * 检查指定人数是否在支持的选项中
     * @param playerOptionsJson 玩家选项JSON字符串
     * @param playerCount 要检查的玩家数量
     * @return 是否支持该人数
     */
    public static boolean isPlayerCountSupported(String playerOptionsJson, int playerCount) {
        List<Integer> options = parsePlayerOptions(playerOptionsJson);
        return options.contains(playerCount);
    }
    
    /**
     * 获取默认玩家数量（取最小值）
     * @param playerOptionsJson 玩家选项JSON字符串
     * @return 默认玩家数量，如果没有选项则返回2
     */
    public static int getDefaultPlayerCount(String playerOptionsJson) {
        List<Integer> options = parsePlayerOptions(playerOptionsJson);
        return options.isEmpty() ? 2 : options.stream().min(Integer::compareTo).orElse(2);
    }
    
    /**
     * 获取最大支持玩家数量
     * @param playerOptionsJson 玩家选项JSON字符串
     * @return 最大玩家数量，如果没有选项则返回2
     */
    public static int getMaxPlayerCount(String playerOptionsJson) {
        List<Integer> options = parsePlayerOptions(playerOptionsJson);
        return options.isEmpty() ? 2 : options.stream().max(Integer::compareTo).orElse(2);
    }
} 
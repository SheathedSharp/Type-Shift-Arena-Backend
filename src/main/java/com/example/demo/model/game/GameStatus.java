/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-29 22:48:59
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-10-29 22:49:04
 */
package com.example.demo.model.game;

public enum GameStatus {
    WAITING,    // 等待玩家加入
    READY,      // 玩家已满，等待开始
    PLAYING,    // 游戏进行中
    FINISHED    // 游戏结束
}
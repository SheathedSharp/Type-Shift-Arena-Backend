/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2025-01-05 16:51:19
 */
package com.example.demo.entity.enums;

public enum MessageType {
    FRIEND_REQUEST("好友请求"),
    GAME_INVITE("游戏邀请"),
    SYSTEM_NOTICE("系统通知");

    private final String description;

    MessageType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2025-01-05 16:51:27
 */
package com.example.demo.entity.enums;

public enum MessageStatus {
    UNREAD("未读"),
    READ("已读"),
    ACCEPTED("已接受"),
    REJECTED("已拒绝");

    private final String description;

    MessageStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
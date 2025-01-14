/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-11-14 17:01:00
 * @LastEditors: SheathedSharp z404878860@163.com
 * @LastEditTime: 2024-11-15 09:58:15
 */
package com.example.demo.model.game;

import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.entity.enums.TextCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;  

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequest {
    private String playerId;
    private String playerName;
    private String playerAvatar;
    private TextLanguage language;
    private TextCategory category;
    private String difficulty;
    private long timestamp;
} 
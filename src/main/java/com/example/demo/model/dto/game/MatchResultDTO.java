/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-30 21:04:59
 */
package com.example.demo.model.dto.game;

import com.example.demo.entity.GameMatch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchResultDTO {
    private String type;        // 消息类型
    private GameMatch match;    // 对局信息
    private int scoreChange;    // 当前玩家的积分变化
    private int oldScore;       // 当前玩家的原积分
}

/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-28 20:03:44
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-10-28 20:03:49
 */
package com.example.demo.controller.game;

import com.example.demo.model.game.GameProgress;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @MessageMapping("/game/progress")
    @SendTo("/topic/game/progress")
    public GameProgress handleProgress(GameProgress progress) {
        // 这里可以添加游戏逻辑，比如验证进度、检查胜利条件等
        return progress;
    }
}

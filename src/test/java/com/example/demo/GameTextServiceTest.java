/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-13 21:43:26
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-13 22:23:22
 */
package com.example.demo.service.game;

import com.example.demo.entity.GameText;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.entity.enums.TextCategory;
import com.example.demo.repository.GameTextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameTextServiceTest {
    @Autowired
    private GameTextService gameTextService;
    
    @Test
    void testGetRandomText() {
        System.out.println("开始测试获取随机文本");

        // 测试中文日常聊天
        String chineseChat = gameTextService.getRandomText(TextLanguage.CHINESE, TextCategory.DAILY_CHAT, "EASY");
        System.out.println("中文聊天文本: " + chineseChat);
        assertNotNull(chineseChat);
        
        // 测试英文学术写作
        String englishAcademic = gameTextService.getRandomText(TextLanguage.ENGLISH, TextCategory.ACADEMIC_WRITING, "MEDIUM");
        System.out.println("英文聊天文本: " + englishAcademic);
        assertNotNull(englishAcademic);
        
        // 测试数学文本
        String mathText = gameTextService.getRandomText(TextLanguage.CHINESE, TextCategory.LATEX_MATH, "HARD");
        System.out.println("数学文本: " + mathText);
        assertTrue(mathText.contains("$"));
        
        // 测试编程文本
        String programmingText = gameTextService.getRandomText(TextLanguage.ENGLISH, TextCategory.PROGRAMMING, "MEDIUM");
        System.out.println("编程文本: " + programmingText);
        assertNotNull(programmingText);
    }
}
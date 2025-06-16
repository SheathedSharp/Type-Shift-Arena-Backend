package com.example.demo.service;

import com.example.demo.model.GuessIdiomRequest;
import com.example.demo.model.GuessIdiomResponse;
import com.example.demo.service.game.GuessIdiomService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 看图识成语服务测试
 */
@SpringBootTest
@Slf4j
public class GuessIdiomServiceTest {

    @Autowired
    private GuessIdiomService guessIdiomService;

    @Test
    public void testGetAllIdioms() {
        Map<String, String> idioms = guessIdiomService.getAllIdioms();
        assertNotNull(idioms);
        assertFalse(idioms.isEmpty());
        assertTrue(idioms.containsKey("老马识途"));
        
        log.info("支持的成语数量: {}", idioms.size());
        idioms.forEach((idiom, meaning) -> 
            log.info("成语: {} - 含义: {}", idiom, meaning));
    }

    @Test
    public void testGetRandomIdiomInfo() {
        Map<String, String> idiomInfo = guessIdiomService.getRandomIdiomInfo();
        assertNotNull(idiomInfo);
        assertTrue(idiomInfo.containsKey("idiom"));
        assertTrue(idiomInfo.containsKey("meaning"));
        
        log.info("随机成语: {} - 含义: {}", 
                 idiomInfo.get("idiom"), idiomInfo.get("meaning"));
    }

    @Test
    public void testGenerateIdiomImageWithoutApiKey() {
        // 测试在没有API密钥的情况下的行为
        GuessIdiomRequest request = new GuessIdiomRequest();
        request.setIdiom("老马识途");
        
        GuessIdiomResponse response = guessIdiomService.generateIdiomImage(request);
        assertNotNull(response);
        
        if (!response.isSuccess()) {
            log.info("由于未配置API密钥，图片生成失败（这是预期的）: {}", response.getErrorMessage());
            assertTrue(response.getErrorMessage().contains("API密钥"));
        } else {
            log.info("图片生成成功！成语: {}, 图片数量: {}", 
                     response.getIdiom(), response.getImageUrls().size());
        }
    }

    @Test
    public void testGenerateImageWithUnsupportedIdiom() {
        GuessIdiomRequest request = new GuessIdiomRequest();
        request.setIdiom("不存在的成语");
        
        GuessIdiomResponse response = guessIdiomService.generateIdiomImage(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.getErrorMessage().contains("不支持的成语"));
        
        log.info("不支持的成语测试通过: {}", response.getErrorMessage());
    }
} 
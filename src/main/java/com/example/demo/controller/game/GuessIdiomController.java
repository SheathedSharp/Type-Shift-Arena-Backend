package com.example.demo.controller.game;

import com.example.demo.model.GuessIdiomRequest;
import com.example.demo.model.GuessIdiomResponse;
import com.example.demo.service.game.GuessIdiomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 看图识成语控制器
 */
@RestController
@RequestMapping("/api/game/guess-idiom")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "看图识成语", description = "看图识成语游戏API")
public class GuessIdiomController {

    private final GuessIdiomService guessIdiomService;

    @PostMapping("/generate")
    @Operation(summary = "生成成语图片", description = "根据指定成语或随机成语生成提示图片")
    public ResponseEntity<GuessIdiomResponse> generateIdiomImage(
            @RequestBody(required = false) GuessIdiomRequest request) {
        
        log.info("收到生成成语图片请求: {}", request);
        
        if (request == null) {
            request = new GuessIdiomRequest();
        }
        
        GuessIdiomResponse response = guessIdiomService.generateIdiomImage(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/generate/random")
    @Operation(summary = "生成随机成语图片", description = "随机选择一个成语并生成提示图片")
    public ResponseEntity<GuessIdiomResponse> generateRandomIdiomImage(
            @Parameter(description = "图片尺寸，默认1024*1024") 
            @RequestParam(defaultValue = "1024*1024") String size,
            @Parameter(description = "生成图片数量，默认1张") 
            @RequestParam(defaultValue = "1") Integer count) {
        
        log.info("收到生成随机成语图片请求, size: {}, count: {}", size, count);
        
        GuessIdiomRequest request = new GuessIdiomRequest();
        request.setSize(size);
        request.setCount(count);
        
        GuessIdiomResponse response = guessIdiomService.generateIdiomImage(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/generate/{idiom}")
    @Operation(summary = "生成指定成语图片", description = "为指定的成语生成提示图片")
    public ResponseEntity<GuessIdiomResponse> generateSpecificIdiomImage(
            @Parameter(description = "要生成图片的成语") 
            @PathVariable String idiom,
            @Parameter(description = "图片尺寸，默认1024*1024") 
            @RequestParam(defaultValue = "1024*1024") String size,
            @Parameter(description = "生成图片数量，默认1张") 
            @RequestParam(defaultValue = "1") Integer count) {
        
        log.info("收到生成指定成语图片请求, idiom: {}, size: {}, count: {}", idiom, size, count);
        
        GuessIdiomRequest request = new GuessIdiomRequest();
        request.setIdiom(idiom);
        request.setSize(size);
        request.setCount(count);
        
        GuessIdiomResponse response = guessIdiomService.generateIdiomImage(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/idioms")
    @Operation(summary = "获取所有支持的成语", description = "获取系统支持的所有成语及其含义")
    public ResponseEntity<Map<String, String>> getAllIdioms() {
        log.info("收到获取所有成语请求");
        Map<String, String> idioms = guessIdiomService.getAllIdioms();
        return ResponseEntity.ok(idioms);
    }

    @GetMapping("/random")
    @Operation(summary = "获取随机成语信息", description = "随机获取一个成语及其含义（不生成图片）")
    public ResponseEntity<Map<String, String>> getRandomIdiom() {
        log.info("收到获取随机成语请求");
        Map<String, String> idiomInfo = guessIdiomService.getRandomIdiomInfo();
        return ResponseEntity.ok(idiomInfo);
    }
} 
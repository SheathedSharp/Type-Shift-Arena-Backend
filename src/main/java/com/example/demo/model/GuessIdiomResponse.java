package com.example.demo.model;

import lombok.Data;
import java.util.List;

/**
 * 看图识成语响应模型
 */
@Data
public class GuessIdiomResponse {
    /**
     * 目标成语
     */
    private String idiom;
    
    /**
     * 成语含义
     */
    private String meaning;
    
    /**
     * 生成的图片URL列表
     */
    private List<String> imageUrls;
    
    /**
     * 任务ID（异步时使用）
     */
    private String taskId;
    
    /**
     * 生成提示词
     */
    private String prompt;
    
    /**
     * 成功标识
     */
    private boolean success;
    
    /**
     * 错误消息
     */
    private String errorMessage;
} 
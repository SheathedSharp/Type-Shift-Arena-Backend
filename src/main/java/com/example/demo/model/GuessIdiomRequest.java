package com.example.demo.model;

import lombok.Data;

/**
 * 看图识成语请求模型
 */
@Data
public class GuessIdiomRequest {
    /**
     * 成语 - 可选，不传则随机生成
     */
    private String idiom;
    
    /**
     * 图片尺寸，默认1024*1024
     */
    private String size = "1024*1024";
    
    /**
     * 生成图片数量，默认1张
     */
    private Integer count = 1;
} 
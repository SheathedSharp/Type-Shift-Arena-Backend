/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-27 15:39:23
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2024-11-02 00:04:14
 */
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;


@RestController
public class HelloController {
    
    @Operation(summary = "Hello World", description = "Returns a simple greeting")
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }
}


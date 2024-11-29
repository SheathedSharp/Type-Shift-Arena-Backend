/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-27 15:39:23
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-10 10:59:04
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
        return "Hello, Spring Boot! Hello, Type shift Arena";
    }
}


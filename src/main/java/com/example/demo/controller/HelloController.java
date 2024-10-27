/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-27 15:39:23
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-10-27 18:29:27
 */
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }
}


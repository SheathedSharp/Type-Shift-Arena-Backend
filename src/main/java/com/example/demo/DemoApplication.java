/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-10-27 15:39:23
 * @LastEditors: SheathedSharp z404878860@163.com
 * @LastEditTime: 2024-10-29 23:05:37
 */
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

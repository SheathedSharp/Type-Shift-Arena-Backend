/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-28 21:12:16
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2024-12-26 15:18:49
 */
package com.example.demo.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.security.JwtAuthenticationFilter;

//  配置Spring Security的安全策略
@Configuration // 配置类，Spring会在启动时加载。
@EnableWebSecurity // 启用Web安全配置
public class SecurityConfig {

    // 从配置文件中读取app.cors.allowed-origins属性的值，并注入到allowedOrigins字段中。
    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    // 添加 JwtAuthenticationFilter 依赖
    private final JwtAuthenticationFilter jwtAuthFilter;

    // 构造函数注入
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    // 安全过滤器链
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // 启用CORS（跨域资源共享），并使用corsConfigurationSource()方法提供的配置
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 禁用CSRF（跨站请求伪造）保护
            .csrf(csrf -> csrf.disable())
            // 添加 JWT 过滤器
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            // 定义了一系列的URL匹配规则，指定哪些请求需要进行身份验证。
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/topic/**").permitAll()
                .requestMatchers("/queue/**").permitAll()
                .requestMatchers("/app/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("api/players/**").permitAll()
                .requestMatchers("/api/friends/**").permitAll()
                .requestMatchers("/hello").permitAll()
                .anyRequest().authenticated() // 其他请求需要身份验证
            )
            // 添加 session 管理，设置为无状态，Spring Security将不会为每个请求创建会话，而是依赖于客户端提供的令牌（如JWT）来验证用户身份
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .build();
    }

    @Bean
    // 配置CORS（跨域资源共享）
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    // 配置认证管理器
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    // 配置密码编码器
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

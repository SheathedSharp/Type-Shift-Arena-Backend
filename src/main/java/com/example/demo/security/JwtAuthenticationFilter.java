/*
 * @Author: SheathedSharp z404878860@163.com
 * @CreateDate: 2024-10-28 21:15:08
 */
package com.example.demo.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//  自定义的过滤器，用于处理基于 JWT（JSON Web Token）的身份验证
@Component //标记为 Spring 组件，使其能够被 Spring 自动扫描并注册为 Bean。
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 继承自 OncePerRequestFilter，确保每次请求只被过滤一次

    private final JwtTokenUtil jwtTokenUtil; // 注入 JwtTokenUtil 用于处理 JWT 相关的操作
    private final UserDetailsService userDetailsService; // 注入 Spring Security 的 UserDetailsService 用于加载用户详细信息

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) // 处理请求和响应
            throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization"); // 获取请求头中的 Authorization 字段

        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // 如果 Authorization 字段不存在或不以 "Bearer " 开头
            chain.doFilter(request, response); // 继续过滤链
            return; // 返回，不进行后续处理
        }

        final String jwt = authHeader.substring(7); // 提取 JWT 字符串，去掉 "Bearer " 前缀
        final String username = jwtTokenUtil.getUsernameFromToken(jwt); // 从 JWT 中提取用户名

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { //如果从 JWT 中提取的用户名不为空且当前安全上下文中没有认证信息，则加载用户详细信息
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwt, userDetails)) { // 验证 JWT 是否有效    
                // 创建一个 UsernamePasswordAuthenticationToken 对象，并将其设置到安全上下文中，表示用户已通过身份验证
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        chain.doFilter(request, response); // 将请求传递给下一个过滤器
    }
}
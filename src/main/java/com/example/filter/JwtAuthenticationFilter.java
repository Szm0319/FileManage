package com.example.filter;

import com.example.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
@WebFilter(filterName = "jwtFilter", urlPatterns = "/api/*")  // 可以指定过滤的URL路径
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;  // 引入 JwtUtil 工具类

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // 直接放行 /user/** 的请求
        if (requestURI.startsWith("/user/")) {
            filterChain.doFilter(request, response);
            log.info("本次请求不需要验证用户身份--");
            return;
        }
        // 从请求头中获取 Authorization 字段和 username 字段
        String token = request.getHeader("Authorization");
        String user = request.getHeader("username");
        log.info("接收当前用户信息: " + user);
        log.info("接收到认证信息: " + token);

        // 如果没有 token 或者 token 格式不对，直接跳过验证
        if (token == null || !token.startsWith("Bearer ")) {
            sendUnauthorizedResponse(response, "Token is missing");
            return;
        }
        token = token.substring(7); // 去除 "Bearer " 部分

        try {
            //检查提取的用户名与提供的用户名是否匹配，并且检查 JWT 是否未过期
            if (!jwtUtil.validateToken(token, user)) {
                sendUnauthorizedResponse(response, "Invalid token or username mismatch");
                return;
            }

            // 提取 token 详情
            Map<String, Object> tokenDetails = jwtUtil.extractTokenDetails(token);
            String username = (String) tokenDetails.get("username");
            Date issuedAt = (Date) tokenDetails.get("issuedAt");
            Date expiration = (Date) tokenDetails.get("expiration");

            log.info("提取认证信息中的用户名：" + username + "------" + "当前用户信息：" + user);
            log.info("Token 签发时间：" + issuedAt);
            log.info("Token 过期时间：" + expiration);

            // 将用户信息存入 SecurityContext（Spring Security 会使用它进行授权）
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException e) {
            sendUnauthorizedResponse(response, "Token has expired");
            log.info("Token已达过期时间");
            return;
        } catch (Exception e) {
            sendUnauthorizedResponse(response, "Invalid token");
            return;
        }

        // 继续执行后续的过滤器链
        filterChain.doFilter(request, response);
    }

    // 提取的帮助方法：统一处理 401 错误响应
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 设置 401 状态码
        response.getWriter().write(message);
    }
}

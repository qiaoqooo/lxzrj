package org.example.userservice.config;

import org.example.common.util.JwtUtil;
import org.example.common.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证拦截器
 * 用于验证 JWT Token 并设置用户上下文
 * 
 * @author user-service
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取 Token（从 Header 中获取）
        String token = request.getHeader("Authorization");
        
        // 如果没有 Token，尝试从请求参数中获取
        if (token == null || token.isEmpty()) {
            token = request.getParameter("token");
        }
        
        // 如果还是没有 Token，返回未授权
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未授权，请先登录\"}");
            return false;
        }

        // 移除 "Bearer " 前缀（如果存在）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证 Token
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token 无效或已过期\"}");
            return false;
        }

        // 从 Token 中获取用户信息并设置到上下文
        Long userId = jwtUtil.getUserIdFromToken(token);
        String openId = jwtUtil.getOpenIdFromToken(token);
        
        if (userId != null) {
            UserContext.setUserId(userId);
        }
        if (openId != null) {
            UserContext.setOpenId(openId);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束后清除用户上下文，防止内存泄漏
        UserContext.clear();
    }
}


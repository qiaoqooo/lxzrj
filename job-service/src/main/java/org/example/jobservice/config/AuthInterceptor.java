package org.example.jobservice.config;

import org.example.common.util.JwtUtil;
import org.example.common.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * job-service 认证拦截器
 * 解析网关转发过来的 JWT，设置当前用户上下文
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PARAM = "token";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只对需要登录的接口做处理，其它接口如果没有 token 也放行
        // 放行：职位/公司公开查询相关接口
        String path = request.getRequestURI();
        if (path.startsWith("/job/list")
                || path.startsWith("/job/detail")
                || path.startsWith("/job/company-jobs")
                || path.startsWith("/company/list")
                || path.startsWith("/company/detail")) {
            return true;
        }

        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (token == null || token.isEmpty()) {
            token = request.getParameter(TOKEN_PARAM);
        }
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未授权，请先登录\"}");
            return false;
        }

        if (token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());
        }

        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token 无效或已过期\"}");
            return false;
        }

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
        UserContext.clear();
    }
}




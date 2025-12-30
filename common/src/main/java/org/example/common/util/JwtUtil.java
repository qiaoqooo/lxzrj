package org.example.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 * 用于生成和解析 JWT Token
 * 
 * @author common
 */
@Component
public class JwtUtil {

    /**
     * JWT 密钥（从配置文件中读取，默认值）
     */
    @Value("${jwt.secret:lxzrj-secret-key-2025}")
    private String secret;

    /**
     * Token 过期时间（毫秒），默认 7 天
     */
    @Value("${jwt.expiration:604800000}")
    private Long expiration;

    /**
     * 生成 Token
     * 
     * @param userId 用户ID
     * @param openId 微信 openid
     * @return Token 字符串
     */
    public String generateToken(Long userId, String openId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("openId", openId);
        return createToken(claims, userId.toString());
    }

    /**
     * 创建 Token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从 Token 中获取用户ID
     * 
     * @param token Token 字符串
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            Object userId = claims.get("userId");
            if (userId instanceof Integer) {
                return ((Integer) userId).longValue();
            } else if (userId instanceof Long) {
                return (Long) userId;
            }
        }
        return null;
    }

    /**
     * 从 Token 中获取 openId
     * 
     * @param token Token 字符串
     * @return openId
     */
    public String getOpenIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return (String) claims.get("openId");
        }
        return null;
    }

    /**
     * 从 Token 中获取 Claims
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证 Token 是否有效
     * 
     * @param token Token 字符串
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims != null && !isTokenExpired(claims);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查 Token 是否过期
     */
    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 获取 Token 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getExpiration() : null;
    }
}


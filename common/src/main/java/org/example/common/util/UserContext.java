package org.example.common.util;

/**
 * 用户上下文工具类
 * 使用 ThreadLocal 存储当前登录用户信息
 * 
 * @author common
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> OPEN_ID = new ThreadLocal<>();

    /**
     * 设置当前用户ID
     */
    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        return USER_ID.get();
    }

    /**
     * 设置当前用户 openId
     */
    public static void setOpenId(String openId) {
        OPEN_ID.set(openId);
    }

    /**
     * 获取当前用户 openId
     */
    public static String getOpenId() {
        return OPEN_ID.get();
    }

    /**
     * 清除当前线程的用户信息
     * 在请求结束后调用，防止内存泄漏
     */
    public static void clear() {
        USER_ID.remove();
        OPEN_ID.remove();
    }
}


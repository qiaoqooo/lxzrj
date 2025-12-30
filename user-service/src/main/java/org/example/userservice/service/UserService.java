package org.example.userservice.service;

import org.example.userservice.dto.LoginRequest;
import org.example.userservice.dto.LoginResponse;
import org.example.userservice.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 小程序用户表 服务类
 * </p>
 *
 * @author 小熊敲敲
 * @since 2025-12-26
 */
public interface UserService extends IService<User> {

    /**
     * 微信小程序登录/注册
     * 
     * @param request 登录请求
     * @return 登录响应（包含 token 和用户信息）
     */
    LoginResponse login(LoginRequest request);
}


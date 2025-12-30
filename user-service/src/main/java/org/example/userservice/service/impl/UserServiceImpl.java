package org.example.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.example.common.util.JwtUtil;
import org.example.userservice.dto.LoginRequest;
import org.example.userservice.dto.LoginResponse;
import org.example.userservice.entity.User;
import org.example.userservice.mapper.UserMapper;
import org.example.userservice.service.UserService;
import org.example.userservice.service.WxService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 小程序用户表 服务实现类
 * </p>
 *
 * @author 小熊敲敲
 * @since 2025-12-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private WxService wxService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request) {
        // 1. 通过 code 获取 openid 和 session_key
        Map<String, String> wxResult = wxService.code2Session(request.getCode());
        String openId = wxResult.get("openid");
        String sessionKey = wxResult.get("session_key");
        String unionId = wxResult.get("unionid");

        if (openId == null || openId.isEmpty()) {
            throw new RuntimeException("获取 openid 失败");
        }

        // 2. 根据 openId 查询用户
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, openId));
        boolean isNewUser = false;

        if (user == null) {
            // 3. 用户不存在，创建新用户
            isNewUser = true;
            user = new User();
            user.setOpenId(openId);
            user.setUnionId(unionId);
            user.setSessionKey(sessionKey); // 注意：实际项目中应该加密存储
            user.setNickName(request.getNickName());
            user.setAvatarUrl(request.getAvatarUrl());
            user.setIsDisabled(false);
            user.setDelFlag(false);
            user.setLastLoginTime(LocalDateTime.now());
            
            // 保存用户
            this.save(user);
        } else {
            // 4. 用户已存在，更新登录信息
            // 使用 UpdateWrapper 更新，避免乐观锁问题
            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(User::getId, user.getId())
                    .set(User::getSessionKey, sessionKey)
                    .set(User::getLastLoginTime, LocalDateTime.now());
            
            // 如果传入了新的昵称或头像，更新
            if (request.getNickName() != null && !request.getNickName().isEmpty()) {
                updateWrapper.set(User::getNickName, request.getNickName());
            }
            if (request.getAvatarUrl() != null && !request.getAvatarUrl().isEmpty()) {
                updateWrapper.set(User::getAvatarUrl, request.getAvatarUrl());
            }
            
            // 执行更新
            this.update(updateWrapper);
            
            // 重新查询用户信息（包含更新后的字段和最新的 version）
            user = this.getById(user.getId());
        }

        // 5. 生成 JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getOpenId());

        // 6. 返回登录响应
        return new LoginResponse(token, user, isNewUser);
    }
}


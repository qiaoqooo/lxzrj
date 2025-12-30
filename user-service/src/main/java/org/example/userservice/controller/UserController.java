package org.example.userservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.result.Result;
import org.example.common.util.UserContext;
import org.example.userservice.dto.LoginRequest;
import org.example.userservice.dto.LoginResponse;
import org.example.userservice.entity.User;
import org.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 小程序用户表 前端控制器
 * </p>
 *
 * @author 小熊敲敲
 * @since 2025-12-26
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/userservice/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("微信小程序登录/注册")
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return Result.success(response);
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("/info")
    public Result<User> getUserInfo() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        User user = userService.getById(userId);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        // 不返回敏感信息
        user.setSessionKey(null);
        return Result.success(user);
    }
}


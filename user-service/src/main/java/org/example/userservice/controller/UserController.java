package org.example.userservice.controller;

import org.example.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 小程序用户表 前端控制器
 * </p>
 *
 * @author code-generator
 * @since 2025-12-26
 */
@RestController
@RequestMapping("/userservice/user")
public class UserController {

    /**
     * 示例接口：返回成功响应（无数据）
     */
    @GetMapping("/test")
    public Result<Void> test() {
        return Result.success();
    }

    /**
     * 示例接口：返回成功响应（有数据）
     */
    @GetMapping("/info")
    public Result<String> getInfo() {
        String data = "用户信息";
        return Result.success(data);
    }

    /**
     * 示例接口：返回失败响应
     */
    @GetMapping("/error")
    public Result<Void> error() {
        return Result.fail("操作失败");
    }
}


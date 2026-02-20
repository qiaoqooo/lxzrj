package org.example.userservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.result.Result;
import org.example.common.util.UserContext;
import org.example.userservice.entity.UserJobPreference;
import org.example.userservice.service.UserJobPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "求职意向管理")
@RestController
@RequestMapping("/userservice/job-preference")
public class UserJobPreferenceController {

    @Autowired
    private UserJobPreferenceService userJobPreferenceService;

    @ApiOperation("获取当前用户求职意向")
    @GetMapping("/detail")
    public Result<UserJobPreference> detail() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        UserJobPreference pref = userJobPreferenceService.getByUserId(userId);
        return Result.success(pref);
    }

    @ApiOperation("保存或更新当前用户求职意向")
    @PostMapping("/save")
    public Result<UserJobPreference> save(@RequestBody UserJobPreference param) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        UserJobPreference saved = userJobPreferenceService.saveOrUpdateForUser(userId, param);
        return Result.success(saved);
    }
}






















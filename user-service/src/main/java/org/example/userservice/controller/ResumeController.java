package org.example.userservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.result.Result;
import org.example.common.util.UserContext;
import org.example.userservice.entity.Resume;
import org.example.userservice.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "在线简历")
@RestController
@RequestMapping("/userservice/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @ApiOperation("获取当前用户在线简历详情")
    @GetMapping("/detail")
    public Result<Resume> detail() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        Resume resume = resumeService.getDefaultResume(userId);
        return Result.success(resume);
    }

    @ApiOperation("保存或更新在线简历")
    @PostMapping("/save")
    public Result<Resume> save(@RequestBody Resume resumeParam) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        Resume saved = resumeService.saveOrUpdateDefault(userId, resumeParam);
        return Result.success(saved);
    }
}






















package org.example.userservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.result.Result;
import org.example.common.util.UserContext;
import org.example.userservice.entity.ResumeAttachment;
import org.example.userservice.service.ResumeAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "附件简历")
@RestController
@RequestMapping("/userservice/resume-attachment")
public class ResumeAttachmentController {

    @Autowired
    private ResumeAttachmentService resumeAttachmentService;

    @ApiOperation("上传附件简历")
    @PostMapping("/upload")
    public Result<ResumeAttachment> upload(@RequestParam("file") MultipartFile file) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        ResumeAttachment attachment = resumeAttachmentService.uploadAttachment(userId, file);
        return Result.success(attachment);
    }

    @ApiOperation("当前用户附件简历列表")
    @GetMapping("/list")
    public Result<List<ResumeAttachment>> list() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        List<ResumeAttachment> list = resumeAttachmentService.listByUser(userId);
        return Result.success(list);
    }

    @ApiOperation("删除附件简历")
    @PostMapping("/delete")
    public Result<Void> delete(@RequestParam("id") Long id) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        boolean ok = resumeAttachmentService.deleteByUser(userId, id);
        if (!ok) {
            return Result.fail("删除失败或无权限");
        }
        return Result.success();
    }

    @ApiOperation("重命名附件简历")
    @PostMapping("/rename")
    public Result<Void> rename(@RequestParam("id") Long id,
                               @RequestParam("fileName") String fileName) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        boolean ok = resumeAttachmentService.renameByUser(userId, id, fileName);
        if (!ok) {
            return Result.fail("重命名失败或无权限");
        }
        return Result.success();
    }
}



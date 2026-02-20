package org.example.userservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.result.Result;
import org.example.common.util.UserContext;
import org.example.userservice.dto.JobApplicationListItemDTO;
import org.example.userservice.entity.JobApplication;
import org.example.userservice.entity.User;
import org.example.userservice.mapper.JobApplicationMapper;
import org.example.userservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 简单的投递管理接口：求职者投递、招聘者查看收到的投递
 */
@RestController
@RequestMapping("/userservice/application")
public class JobApplicationController {

    @Autowired
    private JobApplicationMapper jobApplicationMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 求职者：投递简历到某个职位
     */
    @PostMapping("/apply")
    public Result<Void> apply(@RequestParam("jobId") Long jobId,
                              @RequestParam("recruiterId") Long recruiterId) {
        Long seekerId = UserContext.getUserId();
        if (seekerId == null) {
            return Result.fail(401, "未登录");
        }
        if (jobId == null || recruiterId == null) {
            return Result.fail(400, "缺少参数");
        }

        // 避免自己给自己投递
        if (seekerId.equals(recruiterId)) {
            return Result.fail(400, "不能投递自己发布的职位");
        }

        // 检查是否已投递过
        LambdaQueryWrapper<JobApplication> existWrapper = new LambdaQueryWrapper<>();
        existWrapper.eq(JobApplication::getJobId, jobId)
                .eq(JobApplication::getSeekerId, seekerId);
        JobApplication exist = jobApplicationMapper.selectOne(existWrapper);
        if (exist != null) {
            return Result.fail(409, "你已经投递过该职位了");
        }

        JobApplication app = new JobApplication();
        app.setJobId(jobId);
        app.setSeekerId(seekerId);
        app.setRecruiterId(recruiterId);
        app.setStatus("applied");
        app.setCreatedAt(LocalDateTime.now());
        app.setUpdatedAt(LocalDateTime.now());
        jobApplicationMapper.insert(app);

        return Result.success();
    }

    /**
     * 求职者：查看自己的投递列表（可按 status 筛选）
     */
    @GetMapping("/my-list")
    public Result<List<JobApplicationListItemDTO>> listMyApplications(
            @RequestParam(value = "status", required = false) String status) {
        Long seekerId = UserContext.getUserId();
        if (seekerId == null) {
            return Result.fail(401, "未登录");
        }

        LambdaQueryWrapper<JobApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(JobApplication::getSeekerId, seekerId);
        if (status != null && !status.isEmpty()) {
            wrapper.eq(JobApplication::getStatus, status);
        }
        wrapper.orderByDesc(JobApplication::getCreatedAt);

        List<JobApplication> list = jobApplicationMapper.selectList(wrapper);
        if (list == null || list.isEmpty()) {
            return Result.success(new ArrayList<>());
        }

        List<JobApplicationListItemDTO> dtoList = new ArrayList<>();
        for (JobApplication app : list) {
            JobApplicationListItemDTO dto = new JobApplicationListItemDTO();
            dto.setId(app.getId());
            dto.setJobId(app.getJobId());
            dto.setSeekerId(app.getSeekerId());
            dto.setResumeId(app.getResumeId());
            dto.setResumeAttachmentId(app.getResumeAttachmentId());
            dto.setStatus(app.getStatus());
            dto.setRemark(app.getRemark());
            dto.setCreatedAt(app.getCreatedAt());
            dtoList.add(dto);
        }
        return Result.success(dtoList);
    }

    /**
     * 招聘者：查看自己收到的简历投递列表
     */
    @GetMapping("/my-received")
    public Result<List<JobApplicationListItemDTO>> listMyReceived() {
        Long recruiterId = UserContext.getUserId();
        if (recruiterId == null) {
            return Result.fail(401, "未登录");
        }

        LambdaQueryWrapper<JobApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(JobApplication::getRecruiterId, recruiterId)
                .orderByDesc(JobApplication::getCreatedAt);

        List<JobApplication> list = jobApplicationMapper.selectList(wrapper);
        if (list == null || list.isEmpty()) {
            return Result.success(new ArrayList<>());
        }

        // 预加载求职者信息
        List<Long> seekerIds = list.stream()
                .map(JobApplication::getSeekerId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, User> seekerMap = new HashMap<>();
        if (!seekerIds.isEmpty()) {
            List<User> users = userMapper.selectBatchIds(seekerIds);
            for (User u : users) {
                seekerMap.put(u.getId(), u);
            }
        }

        List<JobApplicationListItemDTO> dtoList = new ArrayList<>();
        for (JobApplication app : list) {
            JobApplicationListItemDTO dto = new JobApplicationListItemDTO();
            dto.setId(app.getId());
            dto.setJobId(app.getJobId());
            dto.setSeekerId(app.getSeekerId());
            dto.setResumeId(app.getResumeId());
            dto.setResumeAttachmentId(app.getResumeAttachmentId());
            dto.setStatus(app.getStatus());
            dto.setRemark(app.getRemark());
            dto.setCreatedAt(app.getCreatedAt());

            User seeker = seekerMap.get(app.getSeekerId());
            if (seeker != null) {
                dto.setSeekerName(seeker.getNickname());
            }

            dtoList.add(dto);
        }

        return Result.success(dtoList);
    }
}



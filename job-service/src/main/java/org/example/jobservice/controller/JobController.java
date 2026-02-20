package org.example.jobservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.result.Result;
import org.example.common.util.UserContext;
import org.example.jobservice.dto.JobListItemDTO;
import org.example.jobservice.entity.Job;
import org.example.jobservice.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/list")
    public Result<Page<JobListItemDTO>> listJobs(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "experienceReq", required = false) String experienceReq,
            @RequestParam(value = "degreeReq", required = false) String degreeReq,
            @RequestParam(value = "salaryMin", required = false) Integer salaryMin,
            @RequestParam(value = "salaryMax", required = false) Integer salaryMax,
            @RequestParam(value = "sortType", defaultValue = "recommend") String sortType,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<JobListItemDTO> page = jobService.pageJobs(keyword, city, experienceReq, degreeReq, salaryMin, salaryMax, sortType, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 职位详情
     */
    @GetMapping("/detail")
    public Result<Job> detail(@RequestParam("id") Long id) {
        if (id == null) {
            return Result.fail(400, "缺少职位ID");
        }
        Job job = jobService.getById(id);
        if (job == null) {
            return Result.fail(404, "职位不存在");
        }
        return Result.success(job);
    }

    /**
     * 当前招聘者的职位列表
     */
    @GetMapping("/my/list")
    public Result<Page<Job>> listMyJobs(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        Page<Job> page = jobService.pageMyJobs(userId, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 某个公司的在招职位列表
     */
    @GetMapping("/company-jobs")
    public Result<Page<JobListItemDTO>> listCompanyJobs(
            @RequestParam("companyId") Long companyId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        Page<JobListItemDTO> page = jobService.pageCompanyJobs(companyId, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 发布职位
     */
    @PostMapping("/publish")
    public Result<Job> publish(@RequestBody Job param) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        try {
            Job job = jobService.publishJob(userId, param);
            if (job == null) {
                return Result.fail(400, "发布职位失败");
            }
            return Result.success(job);
        } catch (IllegalStateException e) {
            // 业务前置校验失败，例如：尚未完善公司资料
            return Result.fail(400, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "发布职位异常：" + e.getMessage());
        }
    }

    /**
     * 更新职位
     */
    @PostMapping("/update")
    public Result<Job> update(@RequestBody Job param) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        Job job = jobService.updateJob(userId, param);
        if (job == null) {
            return Result.fail(403, "无权修改该职位或职位不存在");
        }
        return Result.success(job);
    }

    /**
     * 批量查询职位详情（供其他服务 / 前端聚合使用）
     * 前端传参示例：ids=1,2,3
     */
    @GetMapping("/batch-detail")
    public Result<java.util.List<Job>> batchDetail(@RequestParam("ids") String idsStr) {
        if (idsStr == null || idsStr.trim().isEmpty()) {
            return Result.success(java.util.Collections.emptyList());
        }
        java.util.List<Long> ids = new java.util.ArrayList<>();
        for (String s : idsStr.split(",")) {
            try {
                ids.add(Long.parseLong(s.trim()));
            } catch (NumberFormatException ignore) {
            }
        }
        if (ids.isEmpty()) {
            return Result.success(java.util.Collections.emptyList());
        }
        java.util.List<Job> jobs = jobService.listByIds(ids);
        return Result.success(jobs);
    }

    /**
     * 删除职位
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestParam("id") Long id) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        boolean ok = jobService.deleteJob(userId, id);
        if (!ok) {
            return Result.fail(403, "无权删除该职位或职位不存在");
        }
        return Result.success();
    }
}




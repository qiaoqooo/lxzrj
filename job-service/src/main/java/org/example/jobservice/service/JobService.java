package org.example.jobservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.jobservice.dto.JobListItemDTO;
import org.example.jobservice.entity.Job;

public interface JobService {

    Page<JobListItemDTO> pageJobs(String keyword,
                                  String city,
                                  String experienceReq,
                                  String degreeReq,
                                  Integer salaryMin,
                                  Integer salaryMax,
                                  String sortType,
                                  Integer pageNum,
                                  Integer pageSize);

    /**
     * 分页查询当前招聘者发布的职位
     */
    Page<Job> pageMyJobs(Long recruiterId, Integer pageNum, Integer pageSize);

    /**
     * 分页查询某个公司的在招职位
     */
    Page<JobListItemDTO> pageCompanyJobs(Long companyId, Integer pageNum, Integer pageSize);

    /**
     * 根据主键查询职位详情
     */
    Job getById(Long id);

    /**
     * 发布或更新职位
     */
    Job publishJob(Long recruiterId, Job param);

    /**
     * 更新职位（仅限当前招聘者自己的职位）
     */
    Job updateJob(Long recruiterId, Job param);

    /**
     * 删除职位（仅限当前招聘者自己的职位）
     */
    boolean deleteJob(Long recruiterId, Long jobId);

    /**
     * 根据 ID 列表批量查询职位
     */
    java.util.List<Job> listByIds(java.util.Collection<Long> ids);
}




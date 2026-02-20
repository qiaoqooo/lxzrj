package org.example.jobservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.jobservice.dto.JobListItemDTO;
import org.example.jobservice.entity.Company;
import org.example.jobservice.entity.Job;
import org.example.jobservice.mapper.CompanyMapper;
import org.example.jobservice.mapper.JobMapper;
import org.example.jobservice.service.CompanyService;
import org.example.jobservice.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyService companyService;

    @Override
    public Page<JobListItemDTO> pageJobs(String keyword,
                                         String city,
                                         String experienceReq,
                                         String degreeReq,
                                         Integer salaryMin,
                                         Integer salaryMax,
                                         String sortType,
                                         Integer pageNum,
                                         Integer pageSize) {
        Page<Job> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Job::getStatus, "open");
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Job::getTitle, keyword);
        }
        if (StringUtils.hasText(city)) {
            wrapper.eq(Job::getCity, city);
        }
        if (StringUtils.hasText(experienceReq)) {
            wrapper.eq(Job::getExperienceReq, experienceReq);
        }
        if (StringUtils.hasText(degreeReq)) {
            wrapper.eq(Job::getDegreeReq, degreeReq);
        }
        if (salaryMin != null) {
            wrapper.ge(Job::getSalaryMin, salaryMin);
        }
        if (salaryMax != null) {
            wrapper.le(Job::getSalaryMax, salaryMax);
        }

        // 排序：latest 按发布时间倒序；recommend 按薪资和发布时间综合排序
        if ("latest".equalsIgnoreCase(sortType)) {
            wrapper.orderByDesc(Job::getCreatedAt);
        } else {
            wrapper.orderByDesc(Job::getSalaryMax, Job::getCreatedAt);
        }

        jobMapper.selectPage(page, wrapper);

        // 预加载公司信息，避免 N+1
        Map<Long, Company> companyMap = new HashMap<>();
        page.getRecords().forEach(job -> {
            if (job.getCompanyId() != null && !companyMap.containsKey(job.getCompanyId())) {
                Company company = companyMapper.selectById(job.getCompanyId());
                if (company != null) {
                    companyMap.put(job.getCompanyId(), company);
                }
            }
        });

        Page<JobListItemDTO> result = new Page<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());

        result.setRecords(page.getRecords().stream().map(job -> {
            JobListItemDTO dto = new JobListItemDTO();
            dto.setId(job.getId());
            dto.setTitle(job.getTitle());
            if (job.getSalaryMin() != null && job.getSalaryMax() != null) {
                dto.setSalaryText(job.getSalaryMin() + " - " + job.getSalaryMax() + "k");
            }
            Company company = companyMap.get(job.getCompanyId());
            if (company != null) {
                dto.setCompanyName(company.getName());
                dto.setFinancingStage(company.getFinancingStage());
                dto.setCity(company.getCity());
                dto.setDistrict(company.getDistrict());
            } else {
                dto.setCity(job.getCity());
                dto.setDistrict(job.getDistrict());
            }
            dto.setExperienceReq(job.getExperienceReq());
            dto.setDegreeReq(job.getDegreeReq());
            return dto;
        }).collect(Collectors.toList()));

        return result;
    }

    @Override
    public Page<Job> pageMyJobs(Long recruiterId, Integer pageNum, Integer pageSize) {
        if (recruiterId == null) {
            return new Page<>();
        }
        Page<Job> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Job::getRecruiterId, recruiterId)
                .orderByDesc(Job::getCreatedAt);
        jobMapper.selectPage(page, wrapper);
        return page;
    }

    @Override
    public Page<JobListItemDTO> pageCompanyJobs(Long companyId, Integer pageNum, Integer pageSize) {
        if (companyId == null) {
            return new Page<>();
        }
        Page<Job> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Job::getCompanyId, companyId)
                .eq(Job::getStatus, "open")
                .orderByDesc(Job::getCreatedAt);
        jobMapper.selectPage(page, wrapper);

        // 预加载公司信息（只会有一个）
        Map<Long, Company> companyMap = new HashMap<>();
        page.getRecords().forEach(job -> {
            if (job.getCompanyId() != null && !companyMap.containsKey(job.getCompanyId())) {
                Company company = companyMapper.selectById(job.getCompanyId());
                if (company != null) {
                    companyMap.put(job.getCompanyId(), company);
                }
            }
        });

        Page<JobListItemDTO> result = new Page<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setRecords(page.getRecords().stream().map(job -> {
            JobListItemDTO dto = new JobListItemDTO();
            dto.setId(job.getId());
            dto.setTitle(job.getTitle());
            if (job.getSalaryMin() != null && job.getSalaryMax() != null) {
                dto.setSalaryText(job.getSalaryMin() + " - " + job.getSalaryMax() + "k");
            }
            Company company = companyMap.get(job.getCompanyId());
            if (company != null) {
                dto.setCompanyName(company.getName());
                dto.setFinancingStage(company.getFinancingStage());
                dto.setCity(company.getCity());
                dto.setDistrict(company.getDistrict());
            } else {
                dto.setCity(job.getCity());
                dto.setDistrict(job.getDistrict());
            }
            dto.setExperienceReq(job.getExperienceReq());
            dto.setDegreeReq(job.getDegreeReq());
            return dto;
        }).collect(Collectors.toList()));

        return result;
    }

    @Override
    public Job getById(Long id) {
        if (id == null) {
            return null;
        }
        return jobMapper.selectById(id);
    }

    @Override
    public Job publishJob(Long recruiterId, Job param) {
        if (recruiterId == null || param == null) {
            return null;
        }

        // 绑定当前招聘者所属公司，如果还没填写公司信息，则不允许发布职位
        Company myCompany = companyService.getMyCompany(recruiterId);
        if (myCompany == null || myCompany.getId() == null) {
            throw new IllegalStateException("请先在“公司”页完善公司资料，再发布职位");
        }

        Job job = new Job();
        job.setRecruiterId(recruiterId);
        job.setCompanyId(myCompany.getId());
        job.setTitle(param.getTitle());
        job.setSalaryMin(param.getSalaryMin());
        job.setSalaryMax(param.getSalaryMax());
        job.setCity(param.getCity());
        job.setDistrict(param.getDistrict());
        job.setWorkPlace(param.getWorkPlace());
        job.setExperienceReq(param.getExperienceReq());
        job.setDegreeReq(param.getDegreeReq());
        job.setJobType(param.getJobType());
        job.setDescription(param.getDescription());
        job.setStatus("open");
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        jobMapper.insert(job);
        return job;
    }

    @Override
    public Job updateJob(Long recruiterId, Job param) {
        if (recruiterId == null || param == null || param.getId() == null) {
            return null;
        }
        Job exist = jobMapper.selectById(param.getId());
        if (exist == null || !recruiterId.equals(exist.getRecruiterId())) {
            // 只能修改自己的职位
            return null;
        }
        if (param.getTitle() != null) {
            exist.setTitle(param.getTitle());
        }
        if (param.getSalaryMin() != null) {
            exist.setSalaryMin(param.getSalaryMin());
        }
        if (param.getSalaryMax() != null) {
            exist.setSalaryMax(param.getSalaryMax());
        }
        if (param.getCity() != null) {
            exist.setCity(param.getCity());
        }
        if (param.getDistrict() != null) {
            exist.setDistrict(param.getDistrict());
        }
        if (param.getWorkPlace() != null) {
            exist.setWorkPlace(param.getWorkPlace());
        }
        if (param.getExperienceReq() != null) {
            exist.setExperienceReq(param.getExperienceReq());
        }
        if (param.getDegreeReq() != null) {
            exist.setDegreeReq(param.getDegreeReq());
        }
        if (param.getJobType() != null) {
            exist.setJobType(param.getJobType());
        }
        if (param.getDescription() != null) {
            exist.setDescription(param.getDescription());
        }
        exist.setUpdatedAt(LocalDateTime.now());
        jobMapper.updateById(exist);
        return exist;
    }

    @Override
    public boolean deleteJob(Long recruiterId, Long jobId) {
        if (recruiterId == null || jobId == null) {
            return false;
        }
        Job exist = jobMapper.selectById(jobId);
        if (exist == null || !recruiterId.equals(exist.getRecruiterId())) {
            return false;
        }
        // 简单起见，直接物理删除；也可以改成设置 status = "closed"
        return jobMapper.deleteById(jobId) > 0;
    }

    @Override
    public List<Job> listByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return jobMapper.selectBatchIds(ids);
    }
}



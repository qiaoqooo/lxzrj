package org.example.jobservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.jobservice.dto.CompanyListItemDTO;
import org.example.jobservice.entity.Company;
import org.example.jobservice.entity.Job;
import org.example.jobservice.mapper.CompanyMapper;
import org.example.jobservice.mapper.JobMapper;
import org.example.jobservice.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private JobMapper jobMapper;

    @Override
    public Page<CompanyListItemDTO> pageCompanies(String keyword, String city, Integer pageNum, Integer pageSize) {
        Page<Company> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Company::getName, keyword);
        }
        if (StringUtils.hasText(city)) {
            wrapper.eq(Company::getCity, city);
        }
        wrapper.orderByDesc(Company::getCreatedAt);

        companyMapper.selectPage(page, wrapper);

        Page<CompanyListItemDTO> result = new Page<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());

        result.setRecords(page.getRecords().stream().map(company -> {
            CompanyListItemDTO dto = new CompanyListItemDTO();
            dto.setId(company.getId());
            dto.setName(company.getName());
            dto.setLogoUrl(company.getLogoUrl());
            dto.setCity(company.getCity());
            dto.setDistrict(company.getDistrict());
            dto.setFinancingStage(company.getFinancingStage());
            dto.setStaffSize(company.getStaffSize());
            dto.setIndustry(company.getIndustry());

            // 简单统计一个热招职位和职位总数（可优化为单独 SQL）
            LambdaQueryWrapper<Job> jobWrapper = new LambdaQueryWrapper<>();
            jobWrapper.eq(Job::getCompanyId, company.getId())
                    .eq(Job::getStatus, "open");
            jobWrapper.last("limit 1");
            Job hotJob = jobMapper.selectOne(jobWrapper);
            if (hotJob != null) {
                dto.setHotJobTitle(hotJob.getTitle());
            }
            LambdaQueryWrapper<Job> cntWrapper = new LambdaQueryWrapper<>();
            cntWrapper.eq(Job::getCompanyId, company.getId())
                    .eq(Job::getStatus, "open");
            dto.setHotJobCount(jobMapper.selectCount(cntWrapper).intValue());

            return dto;
        }).collect(Collectors.toList()));

        return result;
    }

    @Override
    public Company getMyCompany(Long ownerUserId) {
        if (ownerUserId == null) {
            return null;
        }
        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Company::getOwnerUserId, ownerUserId)
                .last("limit 1");
        return companyMapper.selectOne(wrapper);
    }

    @Override
    public Company getById(Long id) {
        if (id == null) {
            return null;
        }
        return companyMapper.selectById(id);
    }

    @Override
    public Company saveOrUpdateMyCompany(Long ownerUserId, Company param) {
        if (ownerUserId == null || param == null) {
            return null;
        }
        Company exist = getMyCompany(ownerUserId);
        if (exist == null) {
            exist = new Company();
            exist.setOwnerUserId(ownerUserId);
            exist.setCreatedAt(LocalDateTime.now());
        }
        if (StringUtils.hasText(param.getName())) {
            exist.setName(param.getName());
        }
        if (StringUtils.hasText(param.getLogoUrl())) {
            exist.setLogoUrl(param.getLogoUrl());
        }
        if (StringUtils.hasText(param.getCity())) {
            exist.setCity(param.getCity());
        }
        if (StringUtils.hasText(param.getDistrict())) {
            exist.setDistrict(param.getDistrict());
        }
        if (StringUtils.hasText(param.getAddress())) {
            exist.setAddress(param.getAddress());
        }
        if (StringUtils.hasText(param.getFinancingStage())) {
            exist.setFinancingStage(param.getFinancingStage());
        }
        if (StringUtils.hasText(param.getStaffSize())) {
            exist.setStaffSize(param.getStaffSize());
        }
        if (StringUtils.hasText(param.getIndustry())) {
            exist.setIndustry(param.getIndustry());
        }
        if (StringUtils.hasText(param.getHomepageUrl())) {
            exist.setHomepageUrl(param.getHomepageUrl());
        }
        if (StringUtils.hasText(param.getDescription())) {
            exist.setDescription(param.getDescription());
        }
        exist.setUpdatedAt(LocalDateTime.now());
        if (exist.getId() == null) {
            companyMapper.insert(exist);
        } else {
            companyMapper.updateById(exist);
        }
        return exist;
    }
}



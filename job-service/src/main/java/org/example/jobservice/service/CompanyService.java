package org.example.jobservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.jobservice.dto.CompanyListItemDTO;
import org.example.jobservice.entity.Company;

public interface CompanyService {

    Page<CompanyListItemDTO> pageCompanies(String keyword, String city, Integer pageNum, Integer pageSize);

    /**
     * 获取当前招聘者归属的公司信息（如果不存在则返回 null）
     */
    Company getMyCompany(Long ownerUserId);

    /**
     * 根据主键查询公司
     */
    Company getById(Long id);

    /**
     * 保存或更新当前招聘者的公司信息
     */
    Company saveOrUpdateMyCompany(Long ownerUserId, Company param);
}





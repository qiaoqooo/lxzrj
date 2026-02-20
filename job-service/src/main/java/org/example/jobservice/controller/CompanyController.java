package org.example.jobservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.result.Result;
import org.example.common.util.UserContext;
import org.example.jobservice.dto.CompanyListItemDTO;
import org.example.jobservice.entity.Company;
import org.example.jobservice.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/list")
    public Result<Page<CompanyListItemDTO>> listCompanies(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<CompanyListItemDTO> page = companyService.pageCompanies(keyword, city, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/my/detail")
    public Result<Company> myCompanyDetail() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        Company company = companyService.getMyCompany(userId);
        return Result.success(company);
    }

    /**
     * 根据公司ID查询公司详情（供职位详情等使用）
     */
    @GetMapping("/detail")
    public Result<Company> detail(@RequestParam("id") Long id) {
        if (id == null) {
            return Result.fail(400, "缺少公司ID");
        }
        Company company = companyService.getById(id);
        if (company == null) {
            return Result.fail(404, "公司不存在");
        }
        return Result.success(company);
    }

    @PostMapping("/my/save")
    public Result<Company> saveMyCompany(@RequestBody Company param) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        Company saved = companyService.saveOrUpdateMyCompany(userId, param);
        return Result.success(saved);
    }
}





package org.example.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.userservice.entity.Resume;

public interface ResumeService extends IService<Resume> {

    /**
     * 获取当前用户的默认在线简历
     */
    Resume getDefaultResume(Long userId);

    /**
     * 创建或更新当前用户的在线简历，并标记为默认
     */
    Resume saveOrUpdateDefault(Long userId, Resume resumeParam);
}






















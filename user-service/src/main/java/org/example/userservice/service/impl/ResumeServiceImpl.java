package org.example.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.userservice.entity.Resume;
import org.example.userservice.mapper.ResumeMapper;
import org.example.userservice.service.ResumeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume> implements ResumeService {

    @Override
    public Resume getDefaultResume(Long userId) {
        if (userId == null) {
            return null;
        }
        return this.getOne(new LambdaQueryWrapper<Resume>()
                .eq(Resume::getUserId, userId)
                .eq(Resume::getIsDefault, true)
                .last("limit 1"));
    }

    @Override
    public Resume saveOrUpdateDefault(Long userId, Resume resumeParam) {
        if (userId == null || resumeParam == null) {
            return null;
        }
        Resume exist = getDefaultResume(userId);
        if (exist == null) {
            exist = new Resume();
            exist.setUserId(userId);
            exist.setIsDefault(true);
            exist.setCreatedAt(LocalDateTime.now());
        }
        if (StringUtils.hasText(resumeParam.getTitle())) {
            exist.setTitle(resumeParam.getTitle());
        }
        if (StringUtils.hasText(resumeParam.getName())) {
            exist.setName(resumeParam.getName());
        }
        if (resumeParam.getGender() != null) {
            exist.setGender(resumeParam.getGender());
        }
        if (StringUtils.hasText(resumeParam.getCity())) {
            exist.setCity(resumeParam.getCity());
        }
        if (StringUtils.hasText(resumeParam.getWorkExperience())) {
            exist.setWorkExperience(resumeParam.getWorkExperience());
        }
        exist.setUpdatedAt(LocalDateTime.now());
        this.saveOrUpdate(exist);
        return exist;
    }
}




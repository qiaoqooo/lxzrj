package org.example.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.userservice.entity.UserJobPreference;
import org.example.userservice.mapper.UserJobPreferenceMapper;
import org.example.userservice.service.UserJobPreferenceService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class UserJobPreferenceServiceImpl extends ServiceImpl<UserJobPreferenceMapper, UserJobPreference>
        implements UserJobPreferenceService {

    @Override
    public UserJobPreference getByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return this.getOne(new LambdaQueryWrapper<UserJobPreference>()
                .eq(UserJobPreference::getUserId, userId)
                .last("limit 1"));
    }

    @Override
    public UserJobPreference saveOrUpdateForUser(Long userId, UserJobPreference param) {
        if (userId == null || param == null) {
            return null;
        }
        UserJobPreference exist = getByUserId(userId);
        if (exist == null) {
            exist = new UserJobPreference();
            exist.setUserId(userId);
            exist.setCreatedAt(LocalDateTime.now());
        }
        if (StringUtils.hasText(param.getStatusText())) {
            exist.setStatusText(param.getStatusText());
        }
        if (StringUtils.hasText(param.getExpectedCity())) {
            exist.setExpectedCity(param.getExpectedCity());
        }
        if (StringUtils.hasText(param.getExpectedIndustry())) {
            exist.setExpectedIndustry(param.getExpectedIndustry());
        }
        if (StringUtils.hasText(param.getExpectedJob())) {
            exist.setExpectedJob(param.getExpectedJob());
        }
        if (StringUtils.hasText(param.getExpectedSalary())) {
            exist.setExpectedSalary(param.getExpectedSalary());
        }
        if (param.getIsPublic() != null) {
            exist.setIsPublic(param.getIsPublic());
        }
        exist.setUpdatedAt(LocalDateTime.now());
        this.saveOrUpdate(exist);
        return exist;
    }
}






















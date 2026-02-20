package org.example.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.userservice.entity.UserJobPreference;

public interface UserJobPreferenceService extends IService<UserJobPreference> {

    /**
     * 根据用户 ID 获取求职意向
     */
    UserJobPreference getByUserId(Long userId);

    /**
     * 为指定用户创建或更新求职意向
     */
    UserJobPreference saveOrUpdateForUser(Long userId, UserJobPreference param);
}






















package org.example.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.example.common.util.JwtUtil;
import org.example.userservice.dto.LoginRequest;
import org.example.userservice.dto.LoginResponse;
import org.example.userservice.dto.UserOverviewDTO;
import org.example.userservice.entity.FavoriteJob;
import org.example.userservice.entity.JobApplication;
import org.example.userservice.entity.Resume;
import org.example.userservice.entity.ResumeAttachment;
import org.example.userservice.entity.UserJobPreference;
import org.example.userservice.entity.User;
import org.example.userservice.entity.Message;
import org.example.userservice.mapper.*;
import org.example.userservice.service.UserService;
import org.example.userservice.service.WxService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小程序用户表 服务实现类
 * </p>
 *
 * @author 小熊敲敲
 * @since 2025-12-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private WxService wxService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JobApplicationMapper jobApplicationMapper;

    @Autowired
    private FavoriteJobMapper favoriteJobMapper;

    @Autowired
    private FollowCompanyMapper followCompanyMapper;

    @Autowired
    private ResumeAttachmentMapper resumeAttachmentMapper;

    @Autowired
    private UserJobPreferenceMapper userJobPreferenceMapper;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request) {
        // 1. 通过 code 获取 openid 和 session_key
        Map<String, String> wxResult = wxService.code2Session(request.getCode());
        String openId = wxResult.get("openid");

        if (openId == null || openId.isEmpty()) {
            throw new RuntimeException("获取 openid 失败");
        }

        // 2. 根据 openid 查询用户
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, openId));
        boolean isNewUser = false;

        if (user == null) {
            // 3. 用户不存在，创建新用户
            isNewUser = true;
            user = new User();
            user.setOpenId(openId);
            user.setNickname(request.getNickName());
            user.setAvatarUrl(request.getAvatarUrl());
            // 默认求职者角色
            user.setRole("seeker");
            
            // 保存用户
            this.save(user);
        } else {
            // 4. 用户已存在，更新登录信息
            // 使用 UpdateWrapper 更新，避免乐观锁问题
            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(User::getId, user.getId())
                    ;
            
            // 如果传入了新的昵称或头像，更新
            if (request.getNickName() != null && !request.getNickName().isEmpty()) {
                updateWrapper.set(User::getNickname, request.getNickName());
            }
            if (request.getAvatarUrl() != null && !request.getAvatarUrl().isEmpty()) {
                updateWrapper.set(User::getAvatarUrl, request.getAvatarUrl());
            }
            
            // 执行更新
            this.update(updateWrapper);
            
            // 重新查询用户信息（包含更新后的字段和最新的 version）
            user = this.getById(user.getId());
        }

        // 5. 生成 JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getOpenId());

        // 6. 返回登录响应
        return new LoginResponse(token, user, isNewUser);
    }

    @Override
    public UserOverviewDTO getUserOverview(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            return null;
        }

        UserOverviewDTO dto = new UserOverviewDTO();
        dto.setNickname(user.getNickname());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setRole(user.getRole());

        // 在线简历（默认简历）
        Resume resume = resumeMapper.selectOne(
                new LambdaQueryWrapper<Resume>()
                        .eq(Resume::getUserId, userId)
                        .eq(Resume::getIsDefault, true)
                        .last("limit 1")
        );
        dto.setResumeTitle(resume != null ? resume.getTitle() : "我的在线简历");

        // 职位投递统计
        Long deliveredCount = jobApplicationMapper.selectCount(
                new LambdaQueryWrapper<JobApplication>()
                        .eq(JobApplication::getSeekerId, userId)
        );
        dto.setDeliveredCount(deliveredCount);

        Long interviewPendingCount = jobApplicationMapper.selectCount(
                new LambdaQueryWrapper<JobApplication>()
                        .eq(JobApplication::getSeekerId, userId)
                        .eq(JobApplication::getStatus, "interview_pending")
        );
        dto.setInterviewPendingCount(interviewPendingCount);

        // 沟通过 = 有过消息往来的独立用户数量
        List<Message> myMessages = messageMapper.selectList(
                new LambdaQueryWrapper<Message>()
                        .eq(Message::getSenderId, userId)
                        .or()
                        .eq(Message::getReceiverId, userId)
        );
        long communicatedCount = myMessages.stream()
                .map(m -> java.util.Objects.equals(m.getSenderId(), userId) ? m.getReceiverId() : m.getSenderId())
                .filter(java.util.Objects::nonNull)
                .distinct()
                .count();
        dto.setCommunicatedCount(communicatedCount);

        // 收藏职位
        Long favCount = favoriteJobMapper.selectCount(
                new LambdaQueryWrapper<FavoriteJob>()
                        .eq(FavoriteJob::getUserId, userId)
        );
        dto.setFavoriteJobCount(favCount);

        // 附件简历数量
        Long attachCount = resumeAttachmentMapper.selectCount(
                new LambdaQueryWrapper<ResumeAttachment>()
                        .eq(ResumeAttachment::getUserId, userId)
        );
        dto.setResumeAttachmentCount(attachCount);

        // 求职意向
        UserJobPreference pref = userJobPreferenceMapper.selectOne(
                new LambdaQueryWrapper<UserJobPreference>()
                        .eq(UserJobPreference::getUserId, userId)
                        .last("limit 1")
        );
        if (pref != null) {
            dto.setJobPreferenceStatusText(pref.getStatusText());
        }

        return dto;
    }

    @Override
    public void switchRole(Long userId, String role) {
        if (userId == null || role == null) {
            return;
        }
        // 只允许在 seeker / recruiter 之间切换
        if (!"seeker".equals(role) && !"recruiter".equals(role)) {
            return;
        }
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId)
                .set(User::getRole, role);
        this.update(wrapper);
    }

    @Override
    public void updateNickname(Long userId, String nickname) {
        if (userId == null || nickname == null || nickname.trim().isEmpty()) {
            return;
        }
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId)
                .set(User::getNickname, nickname.trim());
        this.update(wrapper);
    }
}


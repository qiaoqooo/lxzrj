package org.example.userservice.dto;

import lombok.Data;

@Data
public class UserOverviewDTO {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 在线简历标题
     */
    private String resumeTitle;

    /**
     * “沟通过”数量（简单用有进展的投递数代替）
     */
    private Long communicatedCount;

    /**
     * “待面试”数量
     */
    private Long interviewPendingCount;

    /**
     * “已投简历”数量
     */
    private Long deliveredCount;

    /**
     * “收藏”职位数量
     */
    private Long favoriteJobCount;

    /**
     * 附件简历数量
     */
    private Long resumeAttachmentCount;

    /**
     * 求职意向文案，如：离职-随时到岗
     */
    private String jobPreferenceStatusText;

    /**
     * 当前角色：seeker / recruiter / admin
     */
    private String role;
}





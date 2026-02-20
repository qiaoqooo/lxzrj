package org.example.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 招聘者查看收到的投递列表 DTO
 */
@Data
public class JobApplicationListItemDTO {

    private Long id;

    private Long jobId;
    private String jobTitle;

    private Long seekerId;
    private String seekerName;

    private Long resumeId;
    private Long resumeAttachmentId;

    private String status;
    private String remark;

    private LocalDateTime createdAt;
}




















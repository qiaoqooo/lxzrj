package org.example.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("job_applications")
public class JobApplication {

    private Long id;

    @TableField("job_id")
    private Long jobId;

    @TableField("seeker_id")
    private Long seekerId;

    @TableField("recruiter_id")
    private Long recruiterId;

    @TableField("resume_id")
    private Long resumeId;

    @TableField("resume_attachment_id")
    private Long resumeAttachmentId;

    /**
     * applied / communicating / interview_pending / rejected / offered / hired / withdrawn
     */
    private String status;

    private String remark;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}























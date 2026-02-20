package org.example.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_job_preferences")
public class UserJobPreference {

    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("status_text")
    private String statusText;

    @TableField("expected_city")
    private String expectedCity;

    @TableField("expected_industry")
    private String expectedIndustry;

    @TableField("expected_job")
    private String expectedJob;

    @TableField("expected_salary")
    private String expectedSalary;

    @TableField("is_public")
    private Boolean isPublic;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}























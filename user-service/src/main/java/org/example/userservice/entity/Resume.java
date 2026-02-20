package org.example.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("resumes")
public class Resume {

    private Long id;

    @TableField("user_id")
    private Long userId;

    private String title;

    private String name;

    private Integer gender;

    private String city;

    @TableField("work_experience")
    private String workExperience;

    @TableField("is_default")
    private Boolean isDefault;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}





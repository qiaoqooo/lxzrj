package org.example.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("follow_companies")
public class FollowCompany {

    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("company_id")
    private Long companyId;

    @TableField("created_at")
    private LocalDateTime createdAt;
}























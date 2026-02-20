package org.example.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("favorite_jobs")
public class FavoriteJob {

    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("job_id")
    private Long jobId;

    @TableField("created_at")
    private LocalDateTime createdAt;
}























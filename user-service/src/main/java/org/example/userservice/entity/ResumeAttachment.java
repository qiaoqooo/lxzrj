package org.example.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("resume_attachments")
public class ResumeAttachment {

    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("resume_id")
    private Long resumeId;

    @TableField("file_name")
    private String fileName;

    @TableField("file_url")
    private String fileUrl;

    @TableField("file_size")
    private Integer fileSize;

    /**
     * 预览图片地址（例如 PDF 首页截图），用于小程序内嵌展示
     */
    @TableField("preview_image_url")
    private String previewImageUrl;

    @TableField("created_at")
    private LocalDateTime createdAt;
}




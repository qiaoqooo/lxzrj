package org.example.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

/**
 * 站内信/私信消息表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("site_messages")
public class Message extends BaseEntity {

    @TableField("sender_id")
    private Long senderId;

    @TableField("receiver_id")
    private Long receiverId;

    /**
     * 文本内容（简单版只支持文本）
     */
    @TableField("content")
    private String content;

    /**
     * 是否已读
     */
    @TableField("is_read")
    private Boolean isRead;
}



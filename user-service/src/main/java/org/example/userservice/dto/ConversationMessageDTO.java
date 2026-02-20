package org.example.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会话中的单条消息 DTO（用于聊天详情页）
 */
@Data
public class ConversationMessageDTO {

    private Long id;

    /** 发送者ID */
    private Long senderId;

    /** 接收者ID */
    private Long receiverId;

    /** 发送者昵称 */
    private String senderName;

    /** 发送者头像 */
    private String senderAvatar;

    /** 消息内容 */
    private String content;

    /** 当前登录用户是否是发送方 */
    private Boolean isMe;

    private LocalDateTime createdAt;
}















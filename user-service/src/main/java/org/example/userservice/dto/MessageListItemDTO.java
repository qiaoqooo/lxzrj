package org.example.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息列表项 DTO（用于“消息”页展示联系人+最后一条消息）
 */
@Data
public class MessageListItemDTO {

    private Long id;

    private Long senderId;

    private String senderName;

    private String senderAvatar;

    private String content;

    private Boolean read;

    private LocalDateTime createdAt;
}





















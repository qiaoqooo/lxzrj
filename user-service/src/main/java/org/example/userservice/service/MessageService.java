package org.example.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.userservice.dto.ConversationMessageDTO;
import org.example.userservice.dto.MessageListItemDTO;
import org.example.userservice.entity.Message;

import java.util.List;

public interface MessageService extends IService<Message> {

    /**
     * 查询当前用户的会话列表（合并双向消息，每个会话只展示最后一条）
     */
    List<MessageListItemDTO> listInbox(Long userId);

    /**
     * 查询两个用户之间的完整聊天记录（按时间正序）
     */
    List<ConversationMessageDTO> listConversation(Long currentUserId, Long targetUserId);

    /**
     * 发送一条简单文本消息
     */
    void sendMessage(Long senderId, Long receiverId, String content);
}








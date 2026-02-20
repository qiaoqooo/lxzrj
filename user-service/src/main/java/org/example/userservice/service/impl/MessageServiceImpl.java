package org.example.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.userservice.dto.ConversationMessageDTO;
import org.example.userservice.dto.MessageListItemDTO;
import org.example.userservice.entity.Message;
import org.example.userservice.entity.User;
import org.example.userservice.mapper.MessageMapper;
import org.example.userservice.mapper.UserMapper;
import org.example.userservice.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<MessageListItemDTO> listInbox(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        // 查询所有与当前用户相关的消息（发送 + 接收），按时间倒序
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(Message::getReceiverId, userId).or().eq(Message::getSenderId, userId))
                .orderByDesc(Message::getCreatedAt);

        List<Message> allMessages = this.list(wrapper);
        if (allMessages.isEmpty()) {
            return Collections.emptyList();
        }

        // 以"对方ID"为维度合并会话，只保留每个会话的最新一条消息
        Map<Long, Message> latestByPartner = new LinkedHashMap<>();
        for (Message msg : allMessages) {
            // 对方ID = 如果我是发送者，对方就是接收者；反之亦然
            Long partnerId = Objects.equals(msg.getSenderId(), userId)
                    ? msg.getReceiverId()
                    : msg.getSenderId();
            if (partnerId == null) continue;
            if (!latestByPartner.containsKey(partnerId)) {
                latestByPartner.put(partnerId, msg);
            }
        }
        if (latestByPartner.isEmpty()) {
            return Collections.emptyList();
        }

        // 预加载对方用户信息
        Set<Long> partnerIds = latestByPartner.keySet();
        Map<Long, User> userMap = new HashMap<>();
        if (!partnerIds.isEmpty()) {
            List<User> users = userMapper.selectBatchIds(partnerIds);
            for (User u : users) {
                userMap.put(u.getId(), u);
            }
        }

        List<MessageListItemDTO> result = new ArrayList<>();
        for (Map.Entry<Long, Message> entry : latestByPartner.entrySet()) {
            Long partnerId = entry.getKey();
            Message msg = entry.getValue();

            MessageListItemDTO dto = new MessageListItemDTO();
            dto.setId(msg.getId());
            dto.setSenderId(partnerId);          // 这里复用 senderId 字段存放对方ID
            dto.setContent(msg.getContent());
            dto.setRead(Boolean.TRUE.equals(msg.getIsRead()));
            dto.setCreatedAt(msg.getCreatedAt());

            User partner = userMap.get(partnerId);
            if (partner != null) {
                dto.setSenderName(StringUtils.hasText(partner.getNickname()) ? partner.getNickname() : "用户" + partner.getId());
                dto.setSenderAvatar(partner.getAvatarUrl());
            } else {
                dto.setSenderName("用户" + partnerId);
            }
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<ConversationMessageDTO> listConversation(Long currentUserId, Long targetUserId) {
        if (currentUserId == null || targetUserId == null) {
            return Collections.emptyList();
        }

        // 查询两人之间双向的所有消息，按时间正序
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w
                        .and(inner -> inner.eq(Message::getSenderId, currentUserId).eq(Message::getReceiverId, targetUserId))
                        .or(inner -> inner.eq(Message::getSenderId, targetUserId).eq(Message::getReceiverId, currentUserId))
                )
                .orderByAsc(Message::getCreatedAt);

        List<Message> messages = this.list(wrapper);
        if (messages.isEmpty()) {
            return Collections.emptyList();
        }

        // 预加载双方用户信息
        Set<Long> userIds = new HashSet<>();
        userIds.add(currentUserId);
        userIds.add(targetUserId);
        Map<Long, User> userMap = new HashMap<>();
        List<User> users = userMapper.selectBatchIds(userIds);
        for (User u : users) {
            userMap.put(u.getId(), u);
        }

        return messages.stream().map(msg -> {
            ConversationMessageDTO dto = new ConversationMessageDTO();
            dto.setId(msg.getId());
            dto.setSenderId(msg.getSenderId());
            dto.setReceiverId(msg.getReceiverId());
            dto.setContent(msg.getContent());
            dto.setIsMe(Objects.equals(msg.getSenderId(), currentUserId));
            dto.setCreatedAt(msg.getCreatedAt());

            User sender = userMap.get(msg.getSenderId());
            if (sender != null) {
                dto.setSenderName(StringUtils.hasText(sender.getNickname()) ? sender.getNickname() : "用户" + sender.getId());
                dto.setSenderAvatar(sender.getAvatarUrl());
            } else {
                dto.setSenderName("用户" + msg.getSenderId());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void sendMessage(Long senderId, Long receiverId, String content) {
        if (senderId == null || receiverId == null || !StringUtils.hasText(content)) {
            return;
        }
        Message msg = new Message();
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setContent(content);
        msg.setIsRead(false);
        this.save(msg);
    }
}

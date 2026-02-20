package org.example.userservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.result.Result;
import org.example.common.util.UserContext;
import org.example.userservice.dto.ConversationMessageDTO;
import org.example.userservice.dto.MessageListItemDTO;
import org.example.userservice.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "站内信")
@RestController
@RequestMapping("/userservice/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation("当前用户的会话列表（合并双向消息）")
    @GetMapping("/inbox")
    public Result<List<MessageListItemDTO>> inbox() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        List<MessageListItemDTO> list = messageService.listInbox(userId);
        return Result.success(list);
    }

    @ApiOperation("查询与某个用户的聊天记录")
    @GetMapping("/conversation")
    public Result<List<ConversationMessageDTO>> conversation(@RequestParam("targetUserId") Long targetUserId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        List<ConversationMessageDTO> list = messageService.listConversation(userId, targetUserId);
        return Result.success(list);
    }

    @ApiOperation("发送一条站内信（简单文本）")
    @PostMapping("/send")
    public Result<Void> send(@RequestParam("receiverId") Long receiverId,
                             @RequestParam("content") String content) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        messageService.sendMessage(userId, receiverId, content);
        return Result.success();
    }
}

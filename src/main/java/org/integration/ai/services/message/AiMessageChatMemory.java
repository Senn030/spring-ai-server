package org.integration.ai.services.message;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.integration.ai.domain.message.entity.AiMessage;
import org.integration.ai.domain.message.mapper.AiMessageMapper;
import org.integration.ai.exception.BusinessException;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.model.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author senyang
 */
@Service
@AllArgsConstructor
public class AiMessageChatMemory implements ChatMemory {

    @Autowired
    private AiMessageMapper aiMessageMapper;

    @Override
    public void add(String conversationId, Message message) {
        ChatMemory.super.add(conversationId, message);
    }

    @Override
    public void add(String conversationId, List<Message> messages) {

    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        LambdaQueryWrapper<AiMessage> q = new LambdaQueryWrapper<>();
        q.eq(AiMessage::getAiSessionId,conversationId)
                .orderByAsc(AiMessage::getCreatedTime)
                .last("limit "+lastN);
        return aiMessageMapper.selectList(q).stream().map(AiMessageChatMemory::toSpringAiMessage).toList();
    }

    @Override
    public void clear(String conversationId) {
        aiMessageMapper.deleteById(conversationId);
    }

    @SneakyThrows
    public static Media toSpringAiMedia(org.integration.ai.domain.message.entity.Media media) {
        return null;
    }

    public static Message toSpringAiMessage(AiMessage aiMessage) {
        List<Media> mediaList = new ArrayList<>();
        if (!CollectionUtil.isEmpty(aiMessage.getMedias())) {
            mediaList = aiMessage.getMedias().stream().map(AiMessageChatMemory::toSpringAiMedia).toList();
        }
        if (aiMessage.getType().equals(MessageType.ASSISTANT.getValue())) {
            return new AssistantMessage(aiMessage.getTextContent());
        }
        if (aiMessage.getType().equals(MessageType.USER.getValue())) {
            return new UserMessage(aiMessage.getTextContent(), mediaList);
        }
        if (aiMessage.getType().equals(MessageType.SYSTEM.getValue())) {
            return new SystemMessage(aiMessage.getTextContent());
        }
        throw new BusinessException("不支持的消息类型");
    }
}

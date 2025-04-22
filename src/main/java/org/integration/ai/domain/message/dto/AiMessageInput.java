package org.integration.ai.domain.message.dto;

import lombok.Data;
import org.integration.ai.constance.MessageTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史消息
 */
@Data
public class AiMessageInput {
    /**
     * 消息类型(用户/助手/系统)
     */
    private MessageTypeEnum messageType;

    /**
     * 消息内容
     */
    private String textContent;

    // 媒体消息
    private List<Object> medias = new ArrayList<>();

    /**
     * 会话
     */
    private String sessionId;

    private String id;
}

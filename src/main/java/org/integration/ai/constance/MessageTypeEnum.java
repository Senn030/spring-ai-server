package org.integration.ai.constance;

import lombok.Getter;

/**
 * @author senyang
 */

@Getter
public enum MessageTypeEnum {
    // 用户消息
    USER("USER"),
    // AI消息
    ASSISTANT("ASSISTANT"),
    // 系统消息
    SYSTEM("SYSTEM"),
    // 工具消息
    TOOL("TOOL");

    private final String value;

    MessageTypeEnum(String value) {
        this.value = value;
    }

}

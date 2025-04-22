package org.integration.ai.constance;

import com.fasterxml.jackson.annotation.JsonValue;
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

    private final String type;

    MessageTypeEnum(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}

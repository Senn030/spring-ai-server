package org.integration.ai.domain.message.dto;

import lombok.Data;
import org.integration.ai.domain.message.entity.AiMessage;

@Data
public class MessageInputWrapper {
    AiMessage message;

}

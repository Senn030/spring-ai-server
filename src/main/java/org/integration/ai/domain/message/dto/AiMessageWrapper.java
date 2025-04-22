package org.integration.ai.domain.message.dto;

import lombok.Data;

@Data
public class AiMessageWrapper {
    private AiMessageInput message = new AiMessageInput();
    private AiMessageParams params = new AiMessageParams();
}

package org.integration.ai.domain.message.dto;

import lombok.Data;

@Data
public class AiMessageParams {
    Boolean enableVectorStore;
    Boolean enableAgent;
}

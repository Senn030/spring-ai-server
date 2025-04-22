package org.integration.ai.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.integration.ai.domain.message.dto.AiMessageInput;
import org.integration.ai.domain.message.dto.AiMessageWrapper;
import org.integration.ai.services.message.AiMessageServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yangsen
 * @since 2025-04-21
 */
@RequestMapping("/message")
@RestController
@AllArgsConstructor
public class AiMessageController {

    private final AiMessageServiceImpl  aiMessageService;
    private final ObjectMapper objectMapper;

    @DeleteMapping("history/{sessionId}")
    public void deleteHistory(@PathVariable String sessionId) {
        aiMessageService.deleteHistory(sessionId);
    }

    /**
     * 消息保存
     * @param input 用户发送的消息/AI回复的消息
     */
    @PostMapping
    public void save(@RequestBody AiMessageInput input) {
        aiMessageService.save(input);
    }

    @SneakyThrows
    @PostMapping(value = "chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chat(@RequestPart String input,
                                              @RequestPart(required = false) MultipartFile file) {
        AiMessageWrapper aiMessageWrapper = objectMapper.readValue(input, AiMessageWrapper.class);
        String[] functionBeanNames = new String[0];
        // todo agent处理

        return aiMessageService.chat(aiMessageWrapper,functionBeanNames,file);
    }
}

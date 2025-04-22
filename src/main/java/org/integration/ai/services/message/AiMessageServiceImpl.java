package org.integration.ai.services.message;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.integration.ai.domain.message.dto.AiMessageInput;
import org.integration.ai.domain.message.dto.AiMessageWrapper;
import org.integration.ai.domain.message.entity.AiMessage;
import org.integration.ai.domain.message.mapper.AiMessageMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.Media;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import reactor.core.publisher.Flux;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangsen
 * @since 2025-04-21
 */
@Service
@AllArgsConstructor
public class AiMessageServiceImpl extends ServiceImpl<AiMessageMapper, AiMessage> implements IAiMessageService {

    @Autowired
    private AiMessageMapper aiMessageMapper;

    private final ChatModel chatModel;
    private final AiMessageChatMemory chatMemory;
    private final VectorStore vectorStore;
    private final ObjectMapper objectMapper;

    /**
     * 为了支持文件问答，需要同时接收json（AiMessageWrapper json体）和 MultipartFile（文件）
     * Content-Type 从 application/json 修改为 multipart/form-data
     * 之前接收请求参数是用@RequestBody, 现在使用@RequestPart 接收json字符串再手动转成AiMessageWrapper.
     * SpringMVC的@RequestPart是支持自动将Json字符串转换为Java对象，也就是说可以等效`@RequestBody`，
     * 但是由于前端FormData无法设置Part的Content-Type，所以只能手动转json字符串再转成Java对象。
     *
     * @param aiMessageWrapper 消息包含文本信息，会话id，多媒体信息（图片语言）
     * @param file  文件问答
     * @return SSE流
     */
    public Flux<ServerSentEvent<String>> chat(AiMessageWrapper aiMessageWrapper,
                                              String[] functionBeanNames,
                                              MultipartFile file){
        return ChatClient.create(chatModel).prompt()
                // 启用文件问答
                .system(promptSystemSpec -> useFile(promptSystemSpec, file))
                .user(promptUserSpec -> toPrompt(promptUserSpec, aiMessageWrapper.getMessage()))
                // agent列表
                .functions(functionBeanNames)
                .advisors(advisorSpec -> {
                    // 使用历史消息
                    useChatHistory(advisorSpec, aiMessageWrapper.getMessage().getSessionId());
                    // 使用向量数据库
                    useVectorStore(advisorSpec, aiMessageWrapper.getParams().getEnableVectorStore());
                })
                .stream()
                .chatResponse()
                .map(chatResponse -> ServerSentEvent.builder(toJson(chatResponse))
                        // 和前端监听的事件相对应
                        .event("message")
                        .build());
    }

    public void useChatHistory(ChatClient.AdvisorSpec advisorSpec, String sessionId) {
        // 1. 如果需要存储会话和消息到数据库，自己可以实现ChatMemory接口，这里使用自己实现的 AiMessageChatMemory，数据库存储。
        // 2. 传入会话id，MessageChatMemoryAdvisor会根据会话id去查找消息。
        // 3. 只需要携带最近10条消息
        // MessageChatMemoryAdvisor会在消息发送给大模型之前，从ChatMemory中获取会话的历史消息，然后一起发送给大模型。
        advisorSpec.advisors(new MessageChatMemoryAdvisor(chatMemory, sessionId, 10));
    }

    @SneakyThrows
    public void useFile(ChatClient.PromptSystemSpec spec, MultipartFile file) {
        if (file == null) {
            return;
        }
        String content = new TikaDocumentReader(new InputStreamResource(file.getInputStream())).get().get(0).getText();
        assert content != null;
        Message message = new PromptTemplate("""
                已下内容是额外的知识，在你回答问题时可以参考下面的内容
                ---------------------
                {context}
                ---------------------
                """)
                .createMessage(Map.of("context", content));
        spec.text(message.getText());
    }

    public void useVectorStore(ChatClient.AdvisorSpec advisorSpec, Boolean enableVectorStore) {
        if (!enableVectorStore) {
            return;
        }
        // question_answer_context是一个占位符，会替换成向量数据库中查询到的文档。QuestionAnswerAdvisor会替换。
        String promptWithContext = """
                下面是上下文信息
                ---------------------
                {question_answer_context}
                ---------------------
                给定的上下文和提供的历史信息，而不是事先的知识，回复用户的意见。如果答案不在上下文中，告诉用户你不能回答这个问题。
                """;
        advisorSpec.advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().build(), promptWithContext));
    }

    public void toPrompt(ChatClient.PromptUserSpec promptUserSpec, AiMessageInput input) {

        AiMessage aiMessage = new AiMessage();
        aiMessage.setTextContent(input.getTextContent());
        aiMessage.setAiSessionId(input.getSessionId());
        aiMessage.setType(input.getMessageType().getValue());

        // AiMessageInput转成Message
        Message message = AiMessageChatMemory.toSpringAiMessage(aiMessage);
        if (message instanceof UserMessage userMessage &&
                !CollectionUtils.isEmpty(userMessage.getMedia())) {
            // 用户发送的图片/语言
            Media[] medias = new Media[userMessage.getMedia().size()];
            promptUserSpec.media(userMessage.getMedia().toArray(medias));
        }
        // 用户发送的文本
        promptUserSpec.text(message.getText());
    }

    @SneakyThrows
    public String toJson(ChatResponse response) {
        return objectMapper.writeValueAsString(response);
    }


    public void deleteHistory(String sessionId){
        chatMemory.clear(sessionId);
    }

    public void save(AiMessageInput input){
        AiMessage aiMessage = new AiMessage();
        aiMessage.setCreatedTime(LocalDateTime.now());
        aiMessage.setEditedTime(LocalDateTime.now());
        aiMessage.setType(input.getMessageType().getValue());

        aiMessage.setAiSessionId(input.getSessionId());
        aiMessage.setTextContent(input.getTextContent());
        aiMessageMapper.insert(aiMessage);
    }

    public List<AiMessage> findBySessionId(String sessionId , int lastN){
        LambdaQueryWrapper<AiMessage> query = new LambdaQueryWrapper<>();
        query.eq(AiMessage::getAiSessionId,sessionId)
                .orderByAsc(AiMessage::getCreatedTime)
                .last("limit "+lastN);
        return aiMessageMapper.selectList(query);
    }

    public Integer deleteBySessionId(String sessionId){
        LambdaQueryWrapper<AiMessage> query = new LambdaQueryWrapper<>();
        query.eq(AiMessage::getAiSessionId,sessionId);
        return aiMessageMapper.delete(query);
    }
}

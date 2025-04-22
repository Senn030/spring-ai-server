package org.integration.ai.services.session;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import org.integration.ai.domain.message.entity.AiMessage;
import org.integration.ai.domain.message.mapper.AiMessageMapper;
import org.integration.ai.domain.session.dto.AiSessionInput;
import org.integration.ai.domain.session.entity.AiSession;
import org.integration.ai.domain.session.mapper.AiSessionMapper;
import org.integration.ai.exception.BusinessException;
import org.integration.ai.services.session.IAiSessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
public class AiSessionServiceImpl extends ServiceImpl<AiSessionMapper, AiSession> implements IAiSessionService {

    private final AiSessionMapper aiSessionMapper;
    private final AiMessageMapper aiMessageMapper;

    public AiSession findById(String id){
        AiSession aiSession = aiSessionMapper.selectById(id);
        if (aiSession == null){
            throw new BusinessException("会话不存在");
        } else {

            LambdaQueryWrapper<AiMessage> msgQ = new LambdaQueryWrapper<>();
            msgQ.eq(AiMessage::getAiSessionId,aiSession.getId());
            List<AiMessage> messages =aiMessageMapper.selectList(msgQ);
            aiSession.setMessages(messages);
            return aiSession;
        }
    }

    public AiSession save(AiSessionInput input){
        AiSession aiSession = new AiSession();
        aiSession.setName(input.getName());
        aiSession.setCreatorId(StpUtil.getLoginIdAsString());
        aiSession.setEditorId(StpUtil.getLoginIdAsString());
        aiSession.setCreatedTime(LocalDateTime.now());
        aiSession.setEditedTime(LocalDateTime.now());
        aiSessionMapper.insert(aiSession);
        return aiSession;
    }

    public List<AiSession> findByUser(){
        LambdaQueryWrapper<AiSession> query = new LambdaQueryWrapper<>();
        query.eq(AiSession::getCreatorId, StpUtil.getLoginIdAsString())
                .orderByDesc(AiSession::getCreatedTime);
        return aiSessionMapper.selectList(query);
    }

    public int delete(List<String> ids){
        return aiSessionMapper.deleteByIds(ids);
    }
}

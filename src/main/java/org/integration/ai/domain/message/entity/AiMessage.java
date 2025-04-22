package org.integration.ai.domain.message.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yangsen
 * @since 2025-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ai_message")
public class AiMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.NONE)
    private String id;

    private LocalDateTime createdTime;

    private LocalDateTime editedTime;

    private String creatorId;

    private String editorId;

    /**
     * 消息类型(用户/助手/系统)
     */
    private String type;

    /**
     * 消息内容
     */
    private String textContent;

    /**
     * 媒体内容如图片链接、语音链接
     */
    private List<Media> medias;

    /**
     * 会话id
     */
    private String aiSessionId;
}

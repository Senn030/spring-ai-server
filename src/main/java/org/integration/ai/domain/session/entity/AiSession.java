package org.integration.ai.domain.session.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.integration.ai.domain.message.entity.AiMessage;

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
@TableName("ai_session")
public class AiSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.NONE)
    private String id;

    private LocalDateTime createdTime;

    private LocalDateTime editedTime;

    private String creatorId;

    private String editorId;

    /**
     * 会话名称
     */
    private String name;

    @TableField(exist = false)
    private List<AiMessage> messages = new ArrayList<>();
}

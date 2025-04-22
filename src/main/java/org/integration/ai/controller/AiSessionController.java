package org.integration.ai.controller;


import lombok.AllArgsConstructor;
import org.integration.ai.domain.R;
import org.integration.ai.domain.session.dto.AiSessionInput;
import org.integration.ai.domain.session.entity.AiSession;
import org.integration.ai.services.session.AiSessionServiceImpl;
import org.integration.ai.services.session.IAiSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yangsen
 * @since 2025-04-21
 */
@RequestMapping("session")
@RestController
@AllArgsConstructor
public class AiSessionController {

    @Autowired
    public AiSessionServiceImpl aiSessionService;

    /**
     * 根据id查询会话
     * @param id 会话id
     * @return 会话信息
     */
    @GetMapping("{id}")
    public R<AiSession> findById(@PathVariable String id) {
        return R.ok(aiSessionService.findById(id));
    }

    /**
     * 保存会话
     * @param input AiSessionInput
     * @return 创建后的id
     */
    @PostMapping("save")
    public R<String> save(@RequestBody AiSessionInput input) {
        return R.ok(aiSessionService.save(input).getId());
    }

    /**
     * 查询当前登录用户的会话
     *
     * @return 会话列表
     */
    @GetMapping("user")
    public R<List<AiSession>> findByUser() {
        return R.ok(aiSessionService.findByUser());
    }

    /**
     * 批量删除会话
     * @param ids 会话id列表
     */
    @DeleteMapping
    public R<Integer> delete(@RequestBody List<String> ids) {
        return R.ok(aiSessionService.delete(ids));
    }
}

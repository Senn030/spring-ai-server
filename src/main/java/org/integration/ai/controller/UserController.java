package org.integration.ai.controller;


import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import lombok.AllArgsConstructor;
import org.integration.ai.constance.ResultCode;
import org.integration.ai.domain.R;
import org.integration.ai.domain.login.dto.UserLoginInput;
import org.integration.ai.domain.login.dto.UserRegisterInput;
import org.integration.ai.domain.login.entity.User;
import org.integration.ai.services.login.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yangsen
 * @since 2025-04-19
 */
@RequestMapping("user")
@RestController
@AllArgsConstructor
public class UserController {

    @Autowired
    public UserServiceImpl userService;

    @GetMapping()
    public R<User> userInfo() {
        return R.ok(userService.findById(StpUtil.getLoginIdAsString()));
    }

    @PostMapping("/login")
    public R<SaTokenInfo> login(@RequestBody UserLoginInput input) {
        return R.ok(userService.login(input.getPhone(),input.getPassword()));
    }

    @PostMapping("/register")
    public R<SaTokenInfo> register(@RequestBody UserRegisterInput input) {
        return R.ok(userService.register(input.getPhone(),input.getPassword()));
    }
}

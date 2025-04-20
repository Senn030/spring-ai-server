package org.integration.ai.domain.login.dto;


import lombok.Data;

/**
 * Title UserLoginInput$<br>
 * Description UserLoginInput$<br>
 *
 * @author senyang
 * @date 2025-04-19
 * @since 0.0.1
 */  

@Data
public class UserRegisterInput {
    /**
     * 手机号
     */
    private String phone;
    /**
     * 密码
     */
    private String password;
}

package org.integration.ai.exception;

import cn.dev33.satoken.exception.DisableServiceException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import lombok.extern.slf4j.Slf4j;
import org.integration.ai.constance.ResultCode;
import org.integration.ai.domain.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author senyang
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<R<String>> handleBusinessException(BusinessException e) {
        log.error("业务异常", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(R.fail(e.getResultCode(), e.getMessage()));
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<R<String>> handleSystemException(SystemException e) {
        log.error("系统异常", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(R.fail(ResultCode.SystemError));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<R<String>> handleException(Exception e) {
        log.error("系统异常", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(R.fail(ResultCode.SystemError));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<R<String>> handleValidateExceptionForSpring(
        MethodArgumentNotValidException e) {
        log.warn("校验异常", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(R.fail(ResultCode.ValidateError,
                e.getBindingResult().getAllErrors().get(0)
                    .getDefaultMessage()));
    }

    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<R<String>> handleNotLogin(NotLoginException e) {
        log.error("未登录", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(R.fail(ResultCode.Unauthorized));
    }

    @ExceptionHandler(NotRoleException.class)
    public ResponseEntity<R<String>> handleNotRole(NotRoleException e) {
        log.error("角色校验异常", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(R.fail(ResultCode.NotGrant, e.getMessage()));
    }

    @ExceptionHandler(DisableServiceException.class)
    public ResponseEntity<R<String>> handleDisabledException(DisableServiceException e) {
        log.error("账号封禁", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(R.fail(ResultCode.StatusHasInvalid, "账号已被封禁"));
    }


}

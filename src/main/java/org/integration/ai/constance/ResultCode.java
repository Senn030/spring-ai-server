package org.integration.ai.constance;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum ResultCode implements BaseEnum {
    Success(1, "操作成功"),
    Fail(0, "操作失败"),
    NotFindError(10001, "未查询到信息"),
    SaveError(10002, "保存信息失败"),
    UpdateError(10003, "更新信息失败"),
    ValidateError(10004, "数据检验失败"),
    StatusHasValid(10005, "状态已经被启用"),
    StatusHasInvalid(10006, "状态已经被禁用"),
    SystemError(10007, "系统异常"),
    BusinessError(10008, "业务异常"),
    ParamSetIllegal(10009, "参数设置非法"),
    TransferStatusError(10010, "当前状态不正确，请勿重复提交"),
    NotGrant(10011, "权限不足"),
    Unauthorized(10012, "请登录");

    private final Integer code;
    private final String name;

    private ResultCode(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<ResultCode> of(Integer code) {
        return Arrays.stream(values()).filter((resultCode) -> {
            return resultCode.getCode().equals(code);
        }).findFirst();
    }
}


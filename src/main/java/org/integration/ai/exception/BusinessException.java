package org.integration.ai.exception;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.integration.ai.constance.BaseEnum;
import org.integration.ai.constance.ResultCode;

/**
 * @author senyang
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    BaseEnum resultCode;

    public BusinessException(String msg) {
        super(msg);
        this.resultCode = ResultCode.Fail;
    }

    public BusinessException(BaseEnum resultCode) {
        super(resultCode.getName());
        this.resultCode = resultCode;
    }

    public BusinessException(BaseEnum resultCode, String msg) {
        super(msg);
        this.resultCode = resultCode;
    }

}


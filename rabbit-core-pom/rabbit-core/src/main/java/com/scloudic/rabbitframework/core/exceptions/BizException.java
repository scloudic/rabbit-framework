package com.scloudic.rabbitframework.core.exceptions;

import com.scloudic.rabbitframework.core.utils.StatusCode;

/**
 * 定义业务层出错,根据当前业务决定业务流转
 */
public class BizException extends RabbitFrameworkException {
    private static final long serialVersionUID = 9188462797707507030L;
    private StatusCode status = StatusCode.FAIL;

    public BizException() {
        super();
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    @Override
    public StatusCode getStatus() {
        return status;
    }
}

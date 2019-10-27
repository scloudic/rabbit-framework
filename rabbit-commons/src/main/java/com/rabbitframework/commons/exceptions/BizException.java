package com.rabbitframework.commons.exceptions;

import com.rabbitframework.commons.utils.StatusCode;

/**
 * 定义业务层出错,根据当前业务决定业务流转
 */
public class BizException extends RabbitFrameworkException {
    private int status = StatusCode.SC_BIZ_ERROR;

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
    public int getStatus() {
        return status;
    }
}

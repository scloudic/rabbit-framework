package com.rabbitframework.commons.exceptions;

import com.rabbitframework.commons.utils.StatusCode;

/**
 * 定义业务层出错,根据当前业务决定业务流转
 */
public class BizException extends RabbitFrameworkException {
    private static final long serialVersionUID = 9188462797707507030L;
    private static final int DEFAULT_STATUS = StatusCode.FAIL;
    private int status = DEFAULT_STATUS;

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

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getStatus() {
        return status;
    }
}

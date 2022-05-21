package com.scloudic.rabbitframework.core.exceptions;

import com.scloudic.rabbitframework.core.utils.StatusCode;

/**
 * 不知道异常
 *
 * @author: justin
 */
public class UnKnowException extends RabbitFrameworkException {
    private StatusCode status = StatusCode.SC_UN_KNOW;

    public UnKnowException() {
        super();
    }

    public UnKnowException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnKnowException(String message) {
        super(message);
    }

    public UnKnowException(Throwable cause) {
        super(cause);
    }

    @Override
    public StatusCode getStatus() {
        return status;
    }
}

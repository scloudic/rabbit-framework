package com.scloudic.rabbitframework.core.exceptions;

import com.scloudic.rabbitframework.core.utils.StatusCode;

public class ReflectionException extends RabbitFrameworkException {
    private static final long serialVersionUID = 427684446635174629L;
    private StatusCode status = StatusCode.REFLECTION_ERROR;
    public ReflectionException() {
        super();
    }
    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }

    @Override
    public StatusCode getStatus() {
        return status;
    }
}

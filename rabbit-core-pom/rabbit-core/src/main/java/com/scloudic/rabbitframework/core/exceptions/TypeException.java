package com.scloudic.rabbitframework.core.exceptions;

import com.scloudic.rabbitframework.core.utils.StatusCode;

public class TypeException extends RabbitFrameworkException {
    private static final long serialVersionUID = -2957142805761525164L;

    private StatusCode status = StatusCode.TYPE_ERROR;

    public TypeException() {
        super();
    }

    public TypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeException(String message) {
        super(message);
    }

    public TypeException(Throwable cause) {
        super(cause);
    }

    @Override
    public StatusCode getStatus() {
        return status;
    }
}

package com.scloudic.rabbitframework.core.exceptions;

import com.scloudic.rabbitframework.core.utils.StatusCode;

public class AuthcException extends RabbitFrameworkException {
    private StatusCode status = StatusCode.SC_PROXY_AUTHENTICATION_REQUIRED;

    public AuthcException() {
        super();
    }

    public AuthcException(String message, Throwable cause) {
        super(message, cause);
        this.description = message;
    }

    public AuthcException(String message) {
        super(message);
        this.description = message;
    }

    public AuthcException(Throwable cause) {
        super(cause);
    }

    @Override
    public StatusCode getStatus() {
        return status;
    }
}

package com.scloudic.rabbitframework.core.exceptions;

import com.scloudic.rabbitframework.core.utils.StatusCode;

public class AuthzException extends RabbitFrameworkException {
    private StatusCode status = StatusCode.SC_UNAUTHORIZED;

    public AuthzException() {
        super();
    }

    public AuthzException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthzException(String message) {
        super(message);
    }

    public AuthzException(Throwable cause) {
        super(cause);
    }

    @Override
    public StatusCode getStatus() {
        return status;
    }
}

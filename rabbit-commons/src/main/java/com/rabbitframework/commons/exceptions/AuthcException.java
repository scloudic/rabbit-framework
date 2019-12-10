package com.rabbitframework.commons.exceptions;

import com.rabbitframework.commons.utils.StatusCode;

public class AuthcException extends RabbitFrameworkException {
    private static final int DEFAULT_STATUS = StatusCode.SC_PROXY_AUTHENTICATION_REQUIRED;
    private int status = DEFAULT_STATUS;

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

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getStatus() {
        return status;
    }
}

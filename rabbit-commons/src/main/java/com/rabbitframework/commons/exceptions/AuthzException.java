package com.rabbitframework.commons.exceptions;

import com.rabbitframework.commons.utils.StatusCode;

public class AuthzException extends RabbitFrameworkException {
    private static final int DEFAULT_STATUS = StatusCode.SC_UNAUTHORIZED;
    private int status = DEFAULT_STATUS;
    public AuthzException() {
        super();
    }

    public AuthzException(String message, Throwable cause) {
        super(message, cause);
        this.description = message;
    }

    public AuthzException(String message) {
        super(message);
        this.description = message;
    }

    public AuthzException(Throwable cause) {
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

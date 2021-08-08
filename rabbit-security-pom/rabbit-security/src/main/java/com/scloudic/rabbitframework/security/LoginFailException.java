package com.scloudic.rabbitframework.security;

import com.scloudic.rabbitframework.core.exceptions.RabbitFrameworkException;
import com.scloudic.rabbitframework.core.utils.StatusCode;

/**
 * 登陆失败异常
 *
 * @author: justin
 */
public class LoginFailException extends RabbitFrameworkException {
    private StatusCode status = StatusCode.SC_LOGIN_ERROR;

    public LoginFailException() {
        super();
    }

    public LoginFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailException(String message) {
        super(message);
    }

    public LoginFailException(Throwable cause) {
        super(cause);
    }

    @Override
    public StatusCode getStatus() {
        return status;
    }
}

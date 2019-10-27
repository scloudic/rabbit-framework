package com.rabbitframework.security;

import com.rabbitframework.commons.exceptions.RabbitFrameworkException;
import com.rabbitframework.commons.utils.StatusCode;

/**
 * 登陆失败异常
 *
 * @author: justin
 * @date: 2019-06-29 10:29
 */
public class LoginFailException extends RabbitFrameworkException {
	private int status = StatusCode.FAIL;

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
	public int getStatus() {
		return status;
	}
}

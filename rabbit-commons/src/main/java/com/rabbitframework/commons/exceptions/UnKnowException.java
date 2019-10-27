package com.rabbitframework.commons.exceptions;

import com.rabbitframework.commons.utils.StatusCode;

/**
 * 不知道异常
 *
 * @author: justin
 * @date: 2017-08-01 上午1:29
 */
public class UnKnowException extends RabbitFrameworkException {
	private int status = StatusCode.FAIL;

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
	public int getStatus() {
		return status;
	}
}

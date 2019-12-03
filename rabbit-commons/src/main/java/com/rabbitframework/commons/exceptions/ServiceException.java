package com.rabbitframework.commons.exceptions;

import com.rabbitframework.commons.utils.StatusCode;

public class ServiceException extends RabbitFrameworkException {
	private static final long serialVersionUID = 8714902911973669718L;
	private static final int DEFAULT_STATUS = StatusCode.SC_INTERNAL_SERVER_ERROR;
	private int status = DEFAULT_STATUS;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		this.description = message;
	}

	public ServiceException(String message) {
		super(message);
		this.description = message;
	}

	public ServiceException(Throwable cause) {
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

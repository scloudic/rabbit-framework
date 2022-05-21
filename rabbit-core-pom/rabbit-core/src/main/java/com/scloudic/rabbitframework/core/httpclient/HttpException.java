package com.scloudic.rabbitframework.core.httpclient;

public class HttpException extends RuntimeException {
	private static final long serialVersionUID = -2525520123758915476L;

	public HttpException() {
		super();
	}

	public HttpException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpException(String message) {
		super(message);
	}

	public HttpException(Throwable cause) {
		super(cause);
	}
}

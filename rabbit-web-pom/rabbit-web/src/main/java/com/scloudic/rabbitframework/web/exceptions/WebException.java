package com.scloudic.rabbitframework.web.exceptions;

public class WebException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public WebException() {
		super();
	}

	public WebException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebException(String message) {
		super(message);
	}

	public WebException(Throwable cause) {
		super(cause);
	}
}

package com.scloudic.rabbitframework.core.exceptions;

public class ClassException extends RuntimeException {
	private static final long serialVersionUID = 1132218179283683073L;

	public ClassException() {
		super();
	}

	public ClassException(String message) {
		super(message);
	}

	public ClassException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClassException(Throwable cause) {
		super(cause);
	}
}

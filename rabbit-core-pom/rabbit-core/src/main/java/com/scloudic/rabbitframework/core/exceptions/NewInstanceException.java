package com.scloudic.rabbitframework.core.exceptions;

public class NewInstanceException extends BuilderException {
	private static final long serialVersionUID = 9059156636348489365L;

	public NewInstanceException() {
		super();
	}

	public NewInstanceException(String message) {
		super(message);
	}

	public NewInstanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public NewInstanceException(Throwable cause) {
		super(cause);
	}
}

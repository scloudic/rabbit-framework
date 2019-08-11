package com.rabbitframework.web.exceptions;

public class ResourceException extends WebException {
	private static final long serialVersionUID = 1L;
	public ResourceException() {
		super();
	}
	
	public ResourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceException(String message) {
		super(message);
	}
	
	public ResourceException(Throwable cause) {
		super(cause);
	}
}

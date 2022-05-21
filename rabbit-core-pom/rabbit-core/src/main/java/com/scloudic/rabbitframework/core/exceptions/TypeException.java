package com.scloudic.rabbitframework.core.exceptions;

public class TypeException extends RuntimeException {
	private static final long serialVersionUID = -2957142805761525164L;

	public TypeException() {
		super();
	}

	public TypeException(String message) {
		super(message);
	}

	public TypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public TypeException(Throwable cause) {
		super(cause);
	}
}

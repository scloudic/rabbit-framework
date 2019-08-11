package com.rabbitframework.dbase.reflect;

public class ReflectionException extends RuntimeException {
	private static final long serialVersionUID = 427684446635174629L;

	public ReflectionException() {
		super();
	}

	public ReflectionException(String message) {
		super(message);
	}

	public ReflectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReflectionException(Throwable cause) {
		super(cause);
	}
}

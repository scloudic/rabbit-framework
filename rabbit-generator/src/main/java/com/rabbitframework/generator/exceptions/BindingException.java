package com.rabbitframework.generator.exceptions;

public class BindingException extends GeneratorException {
	private static final long serialVersionUID = -8589551402753291377L;

	public BindingException() {
		super();
	}

	public BindingException(String message) {
		super(message);
	}

	public BindingException(String message, Throwable cause) {
		super(message, cause);
	}

	public BindingException(Throwable cause) {
		super(cause);
	}
}

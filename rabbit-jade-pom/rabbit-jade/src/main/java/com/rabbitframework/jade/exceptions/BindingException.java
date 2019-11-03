package com.rabbitframework.jade.exceptions;

public class BindingException extends DbaseException {
	private static final long serialVersionUID = -3092716166519140959L;

	public BindingException() {
		super();
	}

	public BindingException(String msg) {
		super(msg);
	}

	public BindingException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public BindingException(Throwable throwable) {
		super(throwable);
	}
}

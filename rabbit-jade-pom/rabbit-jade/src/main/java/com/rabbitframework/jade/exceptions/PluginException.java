package com.rabbitframework.jade.exceptions;

public class PluginException extends DbaseException {
	private static final long serialVersionUID = 1L;

	public PluginException() {
		super();
	}

	public PluginException(String message) {
		super(message);
	}

	public PluginException(String message, Throwable cause) {
		super(message, cause);
	}

	public PluginException(Throwable cause) {
		super(cause);
	}
}

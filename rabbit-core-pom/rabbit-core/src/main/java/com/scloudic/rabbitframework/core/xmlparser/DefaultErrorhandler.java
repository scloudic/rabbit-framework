package com.scloudic.rabbitframework.core.xmlparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DefaultErrorhandler implements ErrorHandler {
	private static final Logger logger = LoggerFactory.getLogger(DefaultErrorhandler.class);

	public void warning(SAXParseException exception) throws SAXException {
		logger.warn(exception.getMessage(), exception);
	}

	public void error(SAXParseException exception) throws SAXException {
		logger.error(exception.getMessage(), exception);
		throw exception;
	}

	public void fatalError(SAXParseException exception) throws SAXException {
		throw exception;
	}

}

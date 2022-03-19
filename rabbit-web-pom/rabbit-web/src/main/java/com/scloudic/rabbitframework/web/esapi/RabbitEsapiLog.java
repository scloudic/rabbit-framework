package com.scloudic.rabbitframework.web.esapi;

import org.owasp.esapi.Logger;

public class RabbitEsapiLog implements org.owasp.esapi.Logger {
    private org.apache.log4j.Logger logger;
    private int maxLogLevel;

    public RabbitEsapiLog(org.apache.log4j.Logger logger) {
        this.logger = logger;
    }

    private boolean isEnabled(int esapiLevel) {
        //Are Logger.OFF and Logger.ALL reversed?  This should be simply the less than or equal to check...
        return (esapiLevel <= maxLogLevel && maxLogLevel != Logger.OFF) || maxLogLevel == Logger.ALL;
    }

    @Override
    public void always(EventType type, String message) {
        logger.trace(message);
    }

    @Override
    public void always(EventType type, String message, Throwable throwable) {
        logger.trace(message, throwable);
    }

    @Override
    public void trace(EventType type, String message) {
        logger.trace(message);
    }

    @Override
    public void trace(EventType type, String message, Throwable throwable) {
        logger.trace(message, throwable);
    }

    @Override
    public void debug(EventType type, String message) {
        logger.debug(message);
    }

    @Override
    public void debug(EventType type, String message, Throwable throwable) {
        logger.debug(message, throwable);
    }

    @Override
    public void info(EventType type, String message) {
        logger.info(message);
    }

    @Override
    public void info(EventType type, String message, Throwable throwable) {
        logger.info(message, throwable);
    }

    @Override
    public void warning(EventType type, String message) {
        logger.warn(message);
    }

    @Override
    public void warning(EventType type, String message, Throwable throwable) {
        logger.warn(message, throwable);
    }

    @Override
    public void error(EventType type, String message) {
        logger.error(message);
    }

    @Override
    public void error(EventType type, String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    @Override
    public void fatal(EventType type, String message) {
        logger.error(message);
    }

    @Override
    public void fatal(EventType type, String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    @Override
    public int getESAPILevel() {
        return maxLogLevel;
    }

    @Override
    public boolean isTraceEnabled() {
        return isEnabled(Logger.TRACE);
    }

    @Override
    public boolean isDebugEnabled() {
        return isEnabled(Logger.DEBUG);
    }

    @Override
    public boolean isInfoEnabled() {
        return isEnabled(Logger.INFO);
    }

    @Override
    public boolean isWarningEnabled() {
        return isEnabled(Logger.WARNING);
    }

    @Override
    public boolean isErrorEnabled() {
        return isEnabled(Logger.ERROR);
    }

    @Override
    public boolean isFatalEnabled() {
        return isEnabled(Logger.FATAL);
    }


    @Override
    public void setLevel(int level) {
        maxLogLevel = level;
    }

}

package com.scloudic.rabbitframework.web.esapi;

import org.apache.log4j.LogManager;
import org.owasp.esapi.LogFactory;
import org.owasp.esapi.Logger;


public class RabbitEsapiLogFactory implements LogFactory {
    private static final org.apache.log4j.Logger logger = LogManager.getLogger(RabbitEsapiLogFactory.class);
    private static final RabbitEsapiLog rabbitEsapiLog;

    static {
        rabbitEsapiLog = new RabbitEsapiLog(logger);
    }

    @Override
    public Logger getLogger(String moduleName) {
        return rabbitEsapiLog;
    }

    @Override
    public Logger getLogger(Class clazz) {
        return rabbitEsapiLog;
    }

}

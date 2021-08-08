package com.scloudic.rabbitframework.web.springboot.configure;

import org.glassfish.jersey.server.ResourceConfig;

@FunctionalInterface
public interface DefaultResourceConfigCustomizer {
    void customize(ResourceConfig config);
}

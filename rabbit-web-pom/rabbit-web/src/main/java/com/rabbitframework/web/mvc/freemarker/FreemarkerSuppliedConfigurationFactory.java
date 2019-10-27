package com.rabbitframework.web.mvc.freemarker;

import freemarker.template.Configuration;

final class FreemarkerSuppliedConfigurationFactory implements FreemarkerConfigurationFactory {

    private final Configuration configuration;

    public FreemarkerSuppliedConfigurationFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

}

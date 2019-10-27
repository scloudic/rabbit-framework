package com.rabbitframework.web.mvc.freemarker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;

public class FreemarkerDefaultConfigurationFactory implements FreemarkerConfigurationFactory {

    protected final Configuration configuration;

    public FreemarkerDefaultConfigurationFactory(ServletContext servletContext) {
        super();

        // Create different loaders.
        final List<TemplateLoader> loaders = new ArrayList<>();
        if (servletContext != null) {
            loaders.add(new WebappTemplateLoader(servletContext));
        }
        loaders.add(new ClassTemplateLoader(FreemarkerDefaultConfigurationFactory.class, "/"));
        try {
            loaders.add(new FileTemplateLoader(new File("/")));
        } catch (IOException e) {
            // NOOP
        }

        // Create Base configuration.
        configuration = new Configuration();
        configuration.setTemplateLoader(new MultiTemplateLoader(loaders.toArray(new TemplateLoader[loaders.size()])));

    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

}

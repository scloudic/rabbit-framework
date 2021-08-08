package com.scloudic.rabbitframework.web.mvc.freemarker;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.internal.util.PropertiesHelper;
import org.glassfish.jersey.internal.util.collection.Values;
import org.glassfish.jersey.server.ContainerException;
import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.spi.AbstractTemplateProcessor;
import org.springframework.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

final class FreemarkerViewProcessor extends AbstractTemplateProcessor<Template> {

    private final FreemarkerConfigurationFactory factory;
    //
    // /**
    // * Create an instance of this processor with injected
    // * {@link javax.ws.rs.core.Configuration config} and (optional)
    // * {@link javax.servlet.ServletContext servlet context}.
    // *
    // * @param config
    // * config to configure this processor from.
    // * @param serviceLocator
    // * service locator to initialize template object factory if
    // * needed.
    // * @param servletContext
    // * (optional) servlet context to obtain template resources from.
    // */
    // @Inject
    // public FreemarkerViewProcessor(final javax.ws.rs.core.Configuration
    // config, final ServiceLocator serviceLocator,
    // @Optional final ServletContext servletContext) {
    // super(config, servletContext, "freemarker",
    // getSupportedExtensions(config));
    // this.factory = getTemplateObjectFactory(serviceLocator,
    // FreemarkerConfigurationFactory.class,
    // new Value<FreemarkerConfigurationFactory>() {
    // @Override
    // public FreemarkerConfigurationFactory get() {
    // Configuration configuration = getTemplateObjectFactory(serviceLocator,
    // Configuration.class,
    // Values.<Configuration> empty());
    // if (configuration == null) {
    // return new FreemarkerDefaultConfigurationFactory(servletContext);
    // } else {
    // return new FreemarkerSuppliedConfigurationFactory(configuration);
    // }
    // }
    // });
    //
    // }

    /**
     * Create an instance of this processor with injected
     * {@link javax.ws.rs.core.Configuration config} and (optional)
     * {@link javax.servlet.ServletContext servlet context}.
     *
     * @param config           config to configure this processor from.
     * @param injectionManager injection manager.
     */
    @Inject
    public FreemarkerViewProcessor(javax.ws.rs.core.Configuration config, InjectionManager injectionManager) {
        super(config, injectionManager.getInstance(ServletContext.class), "freemarker", getSupportedExtensions(config));
        String templateVariable = FreemarkerMvcFeature.TEMPLATE_BASE_PATH + ".templateVariable";
        final Map<String, Object> properties = config.getProperties();
        final String templateVariablePath = PropertiesHelper.getValue(properties, templateVariable, String.class, null);
        this.factory = getTemplateObjectFactory(injectionManager::createAndInitialize,
                FreemarkerConfigurationFactory.class, () -> {
                    Configuration configuration = getTemplateObjectFactory(injectionManager::createAndInitialize,
                            Configuration.class, Values.empty());
                    if (configuration == null) {
                        return new FreemarkerDefaultConfigurationFactory(
                                injectionManager.getInstance(ServletContext.class), templateVariablePath);
                    } else {
                        return new FreemarkerDefaultConfigurationFactory(configuration, templateVariablePath);
                    }
                });
    }

    private static String[] getSupportedExtensions(final javax.ws.rs.core.Configuration config) {
        String extension = FreemarkerMvcFeature.TEMPLATE_BASE_PATH + ".extensions";
        final Map<String, Object> properties = config.getProperties();
        String basePath = PropertiesHelper.getValue(properties, extension, String.class, null);
        if (StringUtils.isEmpty(basePath)) {
            return new String[]{"ftl"};
        }
        return basePath.split(",");
    }

    @Override
    protected Template resolve(final String templateReference, final Reader reader) throws Exception {
        return factory.getConfiguration().getTemplate(templateReference);
    }

    @Override
    public void writeTo(final Template template, final Viewable viewable, final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders, final OutputStream out) throws IOException {
        try {
            Object model = viewable.getModel();
            if (!(model instanceof Map)) {
                model = new HashMap<String, Object>() {
                    {
                        put("model", viewable.getModel());
                    }
                };
            }
            Charset encoding = setContentType(mediaType, httpHeaders);

            template.process(model, new OutputStreamWriter(out, encoding));
        } catch (TemplateException te) {
            throw new ContainerException(te);
        }
    }
}

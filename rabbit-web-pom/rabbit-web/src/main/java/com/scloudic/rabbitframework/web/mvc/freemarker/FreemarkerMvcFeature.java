package com.scloudic.rabbitframework.web.mvc.freemarker;

import org.glassfish.jersey.server.mvc.MvcFeature;

import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * freemarkerMvc模板功能实现
 *
 * @author justin.liang
 */
@ConstrainedTo(RuntimeType.SERVER)
public class FreemarkerMvcFeature implements Feature {

    private static final String SUFFIX = ".freemarker";

    public static final String TEMPLATE_BASE_PATH = MvcFeature.TEMPLATE_BASE_PATH + SUFFIX;


    public static final String CACHE_TEMPLATES = MvcFeature.CACHE_TEMPLATES + SUFFIX;


    public static final String TEMPLATE_OBJECT_FACTORY = MvcFeature.TEMPLATE_OBJECT_FACTORY + SUFFIX;

    
    public static final String ENCODING = MvcFeature.ENCODING + SUFFIX;

    @Override
    public boolean configure(final FeatureContext context) {
        final Configuration config = context.getConfiguration();

        if (!config.isRegistered(FreemarkerViewProcessor.class)) {
            // Template Processor.
            context.register(FreemarkerViewProcessor.class);

            // MvcFeature.
            if (!config.isRegistered(MvcFeature.class)) {
                context.register(MvcFeature.class);
            }

            return true;
        }
        return false;
    }
}

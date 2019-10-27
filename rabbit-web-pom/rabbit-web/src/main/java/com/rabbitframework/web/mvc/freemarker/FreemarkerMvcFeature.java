package com.rabbitframework.web.mvc.freemarker;
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



    /**
     * {@link String} property defining the base path to Freemarker templates. If set, the value of the property is added in front
     * of the template name defined in:
     * <ul>
     * <li>{@link org.glassfish.jersey.server.mvc.Viewable Viewable}</li>
     * <li>{@link org.glassfish.jersey.server.mvc.Template Template}, or</li>
     * <li>{@link org.glassfish.jersey.server.mvc.ErrorTemplate ErrorTemplate}</li>
     * </ul>
     * <p/>
     * Value can be absolute providing a full path to a system directory with templates or relative to current
     * {@link javax.servlet.ServletContext servlet context}.
     * <p/>
     * There is no default value.
     * <p/>
     * The name of the configuration property is <tt>{@value}</tt>.
     */
    public static final String TEMPLATE_BASE_PATH = MvcFeature.TEMPLATE_BASE_PATH + SUFFIX;

    /**
     * If {@code true} then enable caching of Freemarker templates to avoid multiple compilation.
     * <p/>
     * The default value is {@code false}.
     * <p/>
     * The name of the configuration property is <tt>{@value}</tt>.
     *
     * @since 2.5
     */
    public static final String CACHE_TEMPLATES = MvcFeature.CACHE_TEMPLATES + SUFFIX;

    /**
     * Property used to pass user-configured {@link FreemarkerConfigurationFactory}.
     * <p/>
     * The default value is not set.
     * <p/>
     * The name of the configuration property is <tt>{@value}</tt>.
     * <p/>
     * This property will also accept an instance of {@link freemarker.template.Configuration Configuration} directly, to
     * support backwards compatibility. If you want to set custom {@link freemarker.template.Configuration configuration} then set
     * {@link freemarker.cache.TemplateLoader template loader} to multi loader of:
     * {@link freemarker.cache.WebappTemplateLoader} (if applicable), {@link freemarker.cache.ClassTemplateLoader} and
     * {@link freemarker.cache.FileTemplateLoader} keep functionality of resolving templates.
     * <p/>
     * If no value is set, a {@link FreemarkerDefaultConfigurationFactory factory}
     * with the above behaviour is used by default in the
     * {@link FreemarkerViewProcessor} class.
     * <p/>
     *
     * @since 2.5
     */
    public static final String TEMPLATE_OBJECT_FACTORY = MvcFeature.TEMPLATE_OBJECT_FACTORY + SUFFIX;

    /**
     * Property defines output encoding produced by {@link org.glassfish.jersey.server.mvc.spi.TemplateProcessor}.
     * The value must be a valid encoding defined that can be passed
     * to the {@link java.nio.charset.Charset#forName(String)} method.
     * <p>
     * <p/>
     * The default value is {@code UTF-8}.
     * <p/>
     * The name of the configuration property is <tt>{@value}</tt>.
     * <p/>
     *
     * @since 2.7
     */
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

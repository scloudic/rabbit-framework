package com.rabbitframework.web.springboot.configure;

import com.rabbitframework.web.AbstractContextResource;
import com.rabbitframework.web.annotations.NoProvider;
import com.rabbitframework.web.mvc.freemarker.FreemarkerMvcFeature;
import com.rabbitframework.web.resources.DefaultApplicationConfig;
import com.rabbitframework.web.spring.aop.FormSubmitValidInterceptor;
import com.rabbitframework.web.utils.ServletContextHelper;
import com.tjzq.commons.utils.ClassUtils;
import com.tjzq.commons.utils.StringUtils;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.core.Feature;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
//@AutoConfigureBefore(JerseyAutoConfiguration.class)
@AutoConfigureBefore(RabbitWebFilterAutoConfiguration.class)
@EnableConfigurationProperties(RabbitWebProperties.class)
public class RabbitWebAutoConfiguration {
    //    private static final Logger logger = LoggerFactory.getLogger(RabbitWebAutoConfiguration.class);
    private static String JERSEY_CONFIG_SERVER_MVC_TEMPLATEBASEPATH_JSP = "jersey.config.server.mvc.templateBasePath.jsp";
    private static String JERSEY_CONFIG_SERVER_MVC_TEMPLATEBASEPATH_FREEMARKER = "jersey.config.server.mvc.templateBasePath.freemarker";
    private final RabbitWebProperties rabbitWebProperties;

    public RabbitWebAutoConfiguration(RabbitWebProperties rabbitWebProperties) {
        this.rabbitWebProperties = rabbitWebProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceConfig resourceConfig() {
        Map<String, Object> properties = new HashMap<String, Object>();
        ResourceConfig resourceConfig = new DefaultApplicationConfig();
        resourceConfig.register(FreemarkerMvcFeature.class);
        resourceConfig.register(MultiPartFeature.class);
        resourceConfig.register(JspMvcFeature.class);
        String templatePathJsp = rabbitWebProperties.getJspPath();
        String templatePathFtl = rabbitWebProperties.getFreemarkerPath();
        properties.put("jersey.config.server.mvc.templateBasePath.freemarker.extensions", "html,ftl");
        properties.put("jersey.config.server.wadl.disableWadl", "true");
        properties.put("jersey.config.servlet.filter.staticContentRegex",
                "/(((images|css|js|static|jsp|WEB-INF/jsp)/.*)|(favicon\\.ico))");
        properties.put("jersey.config.server.wadl.disableWadl", "true");
        properties.put("jersey.config.server.provider.scanning.recursive", "true");
        properties.put("jersey.config.servlet.filter.forwardOn404", "true");
        properties.put(JERSEY_CONFIG_SERVER_MVC_TEMPLATEBASEPATH_JSP, templatePathJsp);
        properties.put(JERSEY_CONFIG_SERVER_MVC_TEMPLATEBASEPATH_FREEMARKER, templatePathFtl);
        String packages = rabbitWebProperties.getRestPackages();
        if (StringUtils.isNotBlank(packages)) {
            List<Class<?>> classes = ClassUtils.getClassNamePackage(StringUtils.tokenizeToStringArray(packages));
            int classSize = classes.size();
            for (int i = 0; i < classSize; i++) {
                Class<?> clazz = classes.get(i);
                NoProvider noProvider = clazz.getAnnotation(NoProvider.class);
                if (noProvider != null) {
                    continue;
                }
                Provider provider = clazz.getAnnotation(Provider.class);
                if (provider != null || AbstractContextResource.class.isAssignableFrom(clazz)
                        || Feature.class.isAssignableFrom(clazz)) {
                    resourceConfig.register(clazz);
                }
            }
        }
        resourceConfig.addProperties(properties);
        return resourceConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public FormSubmitValidInterceptor formSubmitValidInterceptor() {
        FormSubmitValidInterceptor formSubmitValidInterceptor = new FormSubmitValidInterceptor();
        return formSubmitValidInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public ServletContextHelper servletContextHelper() {
        ServletContextHelper servletContextHelper = new ServletContextHelper();
        return servletContextHelper;
    }
}
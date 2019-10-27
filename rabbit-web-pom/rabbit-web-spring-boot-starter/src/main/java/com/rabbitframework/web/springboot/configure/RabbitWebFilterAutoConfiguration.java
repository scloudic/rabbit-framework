package com.rabbitframework.web.springboot.configure;

import java.util.Collections;
import java.util.EnumSet;
import javax.annotation.PostConstruct;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.SpringComponentProvider;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.DynamicRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.filter.RequestContextFilter;

import com.rabbitframework.web.servlet.RabbitServletContainer;
import com.rabbitframework.web.springboot.configure.servlet.DefaultRabbitWebApplicationPath;
import com.rabbitframework.web.springboot.configure.servlet.RabbitWebApplicationPath;

@Configuration
@ConditionalOnClass({SpringComponentProvider.class, ServletRegistration.class})
@ConditionalOnBean(ResourceConfig.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(RabbitWebProperties.class)
@AutoConfigureBefore(DispatcherServletAutoConfiguration.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class RabbitWebFilterAutoConfiguration implements ServletContextAware {
    private static final Logger logger = LoggerFactory.getLogger(RabbitWebFilterAutoConfiguration.class);
    private final RabbitWebProperties rabbitWebProperties;
    private final ObjectProvider<DefaultResourceConfigCustomizer> customizers;
    private final ResourceConfig resourceConfig;

    public RabbitWebFilterAutoConfiguration(RabbitWebProperties rabbitWebProperties,
                                            ObjectProvider<DefaultResourceConfigCustomizer> customizers,
                                            ResourceConfig resourceConfig) {
        this.rabbitWebProperties = rabbitWebProperties;
        this.customizers = customizers;
        this.resourceConfig = resourceConfig;
    }

    @PostConstruct
    public void path() {
        customize();
    }

    private void customize() {
        this.customizers.orderedStream().forEach((customizer) -> customizer.customize(this.resourceConfig));
    }

    @Bean
    @ConditionalOnMissingFilterBean(RequestContextFilter.class)
    public FilterRegistrationBean<RequestContextFilter> requestContextFilter() {
        FilterRegistrationBean<RequestContextFilter> registration = new FilterRegistrationBean<RequestContextFilter>();
        registration.setFilter(new RequestContextFilter());
        registration.setOrder(this.rabbitWebProperties.getFilter().getOrder() - 1);
        registration.setName("rabbitWebFilter");
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitWebApplicationPath rabbitWebApplicationPath() {
        return new DefaultRabbitWebApplicationPath(this.rabbitWebProperties.getApplicationPath(), this.resourceConfig);
    }

    @Bean
    @ConditionalOnMissingBean(name = "rabbitWebFilterRegistration")
    @ConditionalOnProperty(prefix = RabbitWebProperties.RABBIT_WEB_PREFIX, name = "type", havingValue = "filter")
    public FilterRegistrationBean<ServletContainer> rabbitWebFilterRegistration(RabbitWebApplicationPath rabbitWebApplicationPath) {
        FilterRegistrationBean<ServletContainer> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RabbitServletContainer(this.resourceConfig));
        registration.setUrlPatterns(Collections.singletonList(rabbitWebApplicationPath.getUrlMapping()));
        registration.setOrder(this.rabbitWebProperties.getFilter().getOrder());
        registration.addInitParameter(ServletProperties.FILTER_CONTEXT_PATH, stripPattern(rabbitWebApplicationPath.getPath()));
        addInitParameters(registration);
        registration.setName("rabbitWebFilter");
        registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return registration;
    }

    private String stripPattern(String path) {
        if (path.endsWith("/*")) {
            path = path.substring(0, path.lastIndexOf("/*"));
        }
        return path;
    }

    @Bean
    @ConditionalOnMissingBean(name = "rabbitWebServletRegistration")
    @ConditionalOnProperty(prefix = RabbitWebProperties.RABBIT_WEB_PREFIX, name = "type", havingValue = "servlet", matchIfMissing = true)
    public ServletRegistrationBean<ServletContainer> rabbitWebServletRegistration(RabbitWebApplicationPath rabbitWebApplicationPath) {
        ServletRegistrationBean<ServletContainer> registration = new ServletRegistrationBean<>(
                new RabbitServletContainer(this.resourceConfig), rabbitWebApplicationPath.getUrlMapping());
        addInitParameters(registration);
        registration.setName(getServletRegistrationName());
        registration.setLoadOnStartup(this.rabbitWebProperties.getServlet().getLoadOnStartup());
        return registration;
    }

    private String getServletRegistrationName() {
        return ClassUtils.getUserClass(this.resourceConfig.getClass()).getName();
    }

    private void addInitParameters(DynamicRegistrationBean<?> registration) {
        this.rabbitWebProperties.getInitParams().forEach(registration::addInitParameter);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        String servletRegistrationName = getServletRegistrationName();
        ServletRegistration registration = servletContext.getServletRegistration(servletRegistrationName);
        if (registration != null) {
            if (logger.isInfoEnabled()) {
                logger.info("Configuring existing registration for Jersey servlet '" + servletRegistrationName + "'");
            }
            registration.setInitParameters(this.rabbitWebProperties.getInitParams());
        }
    }
}

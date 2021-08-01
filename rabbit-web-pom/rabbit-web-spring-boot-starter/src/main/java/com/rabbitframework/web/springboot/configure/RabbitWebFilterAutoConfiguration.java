package com.rabbitframework.web.springboot.configure;

import com.rabbitframework.web.servlet.RabbitServletContainer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.SpringComponentProvider;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.DynamicRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.filter.RequestContextFilter;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.Collections;
import java.util.EnumSet;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SpringComponentProvider.class, ServletRegistration.class})
@ConditionalOnBean(ResourceConfig.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(RabbitWebProperties.class)
@AutoConfigureBefore(DispatcherServletAutoConfiguration.class)
//使用shiro比shiro慢执行
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 2)
public class RabbitWebFilterAutoConfiguration implements ServletContextAware {
    private static final Logger logger = LoggerFactory.getLogger(RabbitWebFilterAutoConfiguration.class);
    private final RabbitWebProperties rabbitWebProperties;
    private final ResourceConfig resourceConfig;

    public RabbitWebFilterAutoConfiguration(RabbitWebProperties rabbitWebProperties,
                                            ObjectProvider<DefaultResourceConfigCustomizer> customizers,
                                            ResourceConfig resourceConfig) {
        this.rabbitWebProperties = rabbitWebProperties;
        this.resourceConfig = resourceConfig;
        customizers.orderedStream().forEach((customizer) -> customizer.customize(this.resourceConfig));
    }


    @Bean
    @ConditionalOnMissingFilterBean(RequestContextFilter.class)
    public FilterRegistrationBean<RequestContextFilter> requestContextFilter() {
        logger.debug("WebrequestContextFilter过滤器加载");
        FilterRegistrationBean<RequestContextFilter> registration = new FilterRegistrationBean<RequestContextFilter>();
        registration.setFilter(new RequestContextFilter());
        registration.setOrder(this.rabbitWebProperties.getFilterOrder().intValue() - 1);
        registration.setName("requestContextFilter");
        return registration;
    }

    @Bean
    @ConditionalOnMissingFilterBean(ServletContainer.class)
    public FilterRegistrationBean<ServletContainer> rabbitWebFilterRegistration() {
        logger.debug("Web过滤器加载");
        FilterRegistrationBean<ServletContainer> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RabbitServletContainer(this.resourceConfig));
        registration.setUrlPatterns(Collections.singletonList("/*"));
        registration.setOrder(this.rabbitWebProperties.getFilterOrder().intValue());
        addInitParameters(registration);
        registration.setName("rabbitWebFilter");
        registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
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

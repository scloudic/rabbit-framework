package com.scloudic.rabbitframework.web.springboot.configure;

import com.scloudic.rabbitframework.core.springboot.configure.RabbitCommonsAutoConfiguration;
import com.scloudic.rabbitframework.core.utils.ClassUtils;
import com.scloudic.rabbitframework.core.utils.CommonResponseUrl;
import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.web.annotations.TemplateVariable;
import com.scloudic.rabbitframework.web.aop.FormSubmitValidInterceptor;
import com.scloudic.rabbitframework.web.aop.RequestLogInterceptor;
import com.scloudic.rabbitframework.web.exceptions.ExceptionMapperSupport;
import com.scloudic.rabbitframework.web.filter.xss.XssFilter;
import com.scloudic.rabbitframework.web.freemarker.ContextPathTag;
import com.scloudic.rabbitframework.web.springboot.RabbitErrorController;
import com.scloudic.rabbitframework.web.utils.ServletContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.List;

@Configuration
@AutoConfigureAfter({RabbitCommonsAutoConfiguration.class, FreeMarkerAutoConfiguration.class})
@EnableConfigurationProperties(RabbitWebProperties.class)
public class RabbitWebAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RabbitWebAutoConfiguration.class);
    private final RabbitWebProperties rabbitWebProperties;
    private ApplicationContext applicationContext;
    @Autowired(required = false)
    private freemarker.template.Configuration configuration;
    @Autowired
    private CommonResponseUrl commonResponseUrl;

    public RabbitWebAutoConfiguration(RabbitWebProperties rabbitWebProperties, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.rabbitWebProperties = rabbitWebProperties;

    }

    @Bean
    public void initFreemarkerTemplate() {
        if (rabbitWebProperties.isFreemarkerEnable()) {
            loadFreemarker();
        }
    }

    private void loadFreemarker() {
        configuration.setSharedVariable("contextPath", new ContextPathTag());
        String templateVariablePath = rabbitWebProperties.getFreemarkerVariablePath();
        if (StringUtils.isBlank(templateVariablePath)) {
            return;
        }
        try {
            List<Class<?>> classes = ClassUtils.getClassNamePackage(StringUtils.tokenizeToStringArray(templateVariablePath));
            int classSize = classes.size();
            for (int i = 0; i < classSize; i++) {
                Class<?> clazz = classes.get(i);
                TemplateVariable templateVariable = clazz.getAnnotation(TemplateVariable.class);
                if (templateVariable == null) {
                    continue;
                }
                String name = templateVariable.value();
                Object object = null;
                Component component = clazz.getAnnotation(Component.class);
                if (component != null) {
                    object = applicationContext.getBean(clazz);
                }
                if (object == null) {
                    object = ClassUtils.newInstance(clazz);
                }
                configuration.setSharedVariable(name, object);
            }
        } catch (Exception e) {
            logger.error("load freemarker template variable error", e);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public FormSubmitValidInterceptor formSubmitValidInterceptor() {
        FormSubmitValidInterceptor formSubmitValidInterceptor = new FormSubmitValidInterceptor();
        return formSubmitValidInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = RabbitWebProperties.RABBIT_WEB_PREFIX, name = "request-log-enable",
            havingValue = "true", matchIfMissing = true)
    public RequestLogInterceptor logInterceptor() {
        RequestLogInterceptor logInterceptor = new RequestLogInterceptor();
        return logInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public ServletContextHelper servletContextHelper() {
        ServletContextHelper servletContextHelper = new ServletContextHelper();
        return servletContextHelper;
    }

    @Bean
    @ConditionalOnProperty(prefix = RabbitWebProperties.RABBIT_WEB_PREFIX, name = "xss-filter-enable",
            havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<Filter> xssFilterRegistration() {
        logger.debug("xss过滤器加载");
        XssFilter xssFilter = new XssFilter();
        xssFilter.setExcludeXssUri(this.rabbitWebProperties.getExcludeXssUri());
        xssFilter.setJsonFilter(rabbitWebProperties.isJsonXssFilterEnable());
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(xssFilter);
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionMapperSupport exceptionMapperSupport() {
        ExceptionMapperSupport exceptionMapperSupport = new ExceptionMapperSupport();
        exceptionMapperSupport.setFrontBlack(commonResponseUrl.isFrontBlack());
        exceptionMapperSupport.setLoginUrl(commonResponseUrl.getLoginUrl());
        exceptionMapperSupport.setOtherError(commonResponseUrl.getOtherError());
        exceptionMapperSupport.setSys404ErrorUrl(commonResponseUrl.getSys404ErrorUrl());
        exceptionMapperSupport.setSys405ErrorUrl(commonResponseUrl.getSys405ErrorUrl());
        exceptionMapperSupport.setSys500ErrorUrl(commonResponseUrl.getSys500ErrorUrl());
        exceptionMapperSupport.setUnauthorizedUrl(commonResponseUrl.getUnauthorizedUrl());
        return exceptionMapperSupport;
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitErrorController rabbitErrorController(ServerProperties serverProperties) {
        RabbitErrorController exceptionMapperSupport = new RabbitErrorController(serverProperties);
        exceptionMapperSupport.setCommonResponseUrl(commonResponseUrl);
        return exceptionMapperSupport;
    }
}
package com.scloudic.rabbitframework.security.springboot.configure;

import com.scloudic.rabbitframework.core.springboot.configure.RabbitCommonsAutoConfiguration;
import com.scloudic.rabbitframework.core.utils.ClassUtils;
import com.scloudic.rabbitframework.core.utils.CommonResponseUrl;
import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.security.spring.web.SecurityFilterFactoryBean;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(RabbitSecurityProperties.class)
@AutoConfigureAfter({SecurityWebAutoConfiguration.class, RabbitCommonsAutoConfiguration.class})
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 1)
public class SecurityFilterAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SecurityFilterAutoConfiguration.class);
    private RabbitSecurityProperties rabbitSecurityProperties;
    @Autowired
    protected SecurityManager securityManager;

    private ApplicationContext applicationContext;
    @Autowired
    private CommonResponseUrl commonResponseUrl;

    public SecurityFilterAutoConfiguration(ApplicationContext applicationContext,
                                           RabbitSecurityProperties rabbitSecurityProperties) {
        this.applicationContext = applicationContext;
        this.rabbitSecurityProperties = rabbitSecurityProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public MethodInvokingFactoryBean getMethodInvokingFactoryBean() {
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("com.scloudic.rabbitframework.security.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(securityManager);
        return factoryBean;
    }

    @Bean
    @ConditionalOnMissingBean
    protected SecurityFilterFactoryBean rabbitSecurityFilterFactoryBean() throws Exception {
        SecurityFilterFactoryBean securityFilterFactoryBean = new SecurityFilterFactoryBean();
        securityFilterFactoryBean.setSecurityManager(securityManager);
        securityFilterFactoryBean.setLoginUrl(commonResponseUrl.getLoginUrl());
        boolean frontEndSeparate = commonResponseUrl.isFrontBlack();
        securityFilterFactoryBean.setFrontEndSeparate(frontEndSeparate);
        Map<String, Filter> filterMap = new HashMap<>();
        Map<String, FilterProperties> filterPropertiesMap = rabbitSecurityProperties.getFilters();
        for (Map.Entry<String, FilterProperties> entry : filterPropertiesMap.entrySet()) {
            FilterProperties filterProperties = entry.getValue();
            Filter filter = null;
            switch (filterProperties.getNameType()) {
                case beanName:
                    filter = (Filter) applicationContext.getBean(filterProperties.getName());
                    break;
                case className:
                    filter = ClassUtils.newInstance(filterProperties.getName());
                    break;
            }

            if (filter != null) {
                filterMap.put(entry.getKey(), filter);
            }

        }

        if (filterMap.size() > 0) {
            securityFilterFactoryBean.setFilters(filterMap);
        }
        securityFilterFactoryBean.setUnauthorizedUrl(commonResponseUrl.getUnauthorizedUrl());
        securityFilterFactoryBean.setSuccessUrl(rabbitSecurityProperties.getSuccessUrl());
        String filterUrls = rabbitSecurityProperties.getFilterUrls();
        if (StringUtils.isNotBlank(filterUrls)) {
            securityFilterFactoryBean.setFilterUrls(rabbitSecurityProperties.getFilterUrls());
        }
        Map<String, String> filterChainDefinitions = rabbitSecurityProperties.getFilterChainDefinitions();
        Map<String, String> filterChainDefinitionMap = null;
        Map<String, String> allMap = new LinkedHashMap<>();

        if (filterChainDefinitions != null && filterChainDefinitions.size() > 0) {
            filterChainDefinitionMap = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : filterChainDefinitions.entrySet()) {
                String permsKey = entry.getKey();
                if (permsKey.indexOf(".") > 0) {
                    permsKey = permsKey.replace(".", "[") + "]";
                }
                String urlValue = entry.getValue();
                String[] urls = urlValue.split(",");
                for (String url : urls) {
                    if ("/**".equalsIgnoreCase(url)) {
                        String allParams = allMap.get(url);
                        if (StringUtils.isNotBlank(allParams)) {
                            allParams += "," + permsKey;
                        } else {
                            allParams = permsKey;
                        }
                        allMap.put(url, allParams);
                    } else {
                        String allParams = filterChainDefinitionMap.get(url);
                        if (StringUtils.isNotBlank(allParams)) {
                            allParams += "," + permsKey;
                        } else {
                            allParams = permsKey;
                        }
                        filterChainDefinitionMap.put(url, allParams);
                    }
                }
                if (allMap.size() > 0) {
                    filterChainDefinitionMap.putAll(allMap);
                }
            }
        }
        securityFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return securityFilterFactoryBean;
    }

    @Bean(name = "filterSecurityFilterRegistrationBean")
    @ConditionalOnMissingBean
    protected FilterRegistrationBean<Filter> filterSecurityFilterRegistrationBean() throws Exception {
        logger.debug("安全过滤器加载");
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD,
                DispatcherType.INCLUDE, DispatcherType.ERROR);
        filterRegistrationBean.setFilter((AbstractShiroFilter) rabbitSecurityFilterFactoryBean().getObject());
        filterRegistrationBean.setOrder(-10);
        return filterRegistrationBean;
    }
}

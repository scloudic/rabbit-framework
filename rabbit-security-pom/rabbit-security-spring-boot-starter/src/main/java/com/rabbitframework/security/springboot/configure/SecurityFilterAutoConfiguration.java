package com.rabbitframework.security.springboot.configure;

import com.rabbitframework.core.springboot.configure.RabbitCommonsAutoConfiguration;
import com.rabbitframework.core.utils.CommonResponseUrl;
import com.rabbitframework.security.spring.web.SecurityFilterFactoryBean;
import com.rabbitframework.core.utils.StringUtils;
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

    public SecurityFilterAutoConfiguration(RabbitSecurityProperties rabbitSecurityProperties) {
        this.rabbitSecurityProperties = rabbitSecurityProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public MethodInvokingFactoryBean getMethodInvokingFactoryBean() {
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("com.rabbitframework.security.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(securityManager);
        return factoryBean;
    }

    @Bean
    @ConditionalOnMissingBean
    protected SecurityFilterFactoryBean rabbitSecurityFilterFactoryBean() throws Exception {
        SecurityFilterFactoryBean securityFilterFactoryBean = new SecurityFilterFactoryBean();
        securityFilterFactoryBean.setSecurityManager(securityManager);
        securityFilterFactoryBean.setLoginUrl(CommonResponseUrl.getLoginUrl());
        securityFilterFactoryBean.setUnauthorizedUrl(CommonResponseUrl.getUnauthorizedUrl());
        String filterUrls = rabbitSecurityProperties.getFilterUrls();
        if (!"no".equalsIgnoreCase(filterUrls)) {
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

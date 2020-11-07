package com.rabbitframework.security.springboot.configure;

import com.rabbitframework.security.spring.web.SecurityFilterFactoryBean;
import com.tjzq.commons.utils.StringUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(RabbitSecurityProperties.class)
@AutoConfigureAfter({SecurityAutoConfiguration.class})
public class SecurityAutoFilterConfiguration {
    private RabbitSecurityProperties rabbitSecurityProperties;
    @Autowired
    protected SecurityManager securityManager;

    public SecurityAutoFilterConfiguration(RabbitSecurityProperties rabbitSecurityProperties) {
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
        securityFilterFactoryBean.setLoginUrl(rabbitSecurityProperties.getLoginUrl());
        securityFilterFactoryBean.setUnauthorizedUrl(rabbitSecurityProperties.getUnauthorizedUrl());
        securityFilterFactoryBean.setFilterUrls(rabbitSecurityProperties.getFilterUrls());
        Map<String, String> filterChainDefinitions = rabbitSecurityProperties.getFilterChainDefinitions();
        Map<String, String> filterChainDefinitionMap = null;
        if (filterChainDefinitions != null && filterChainDefinitions.size() > 0) {
            filterChainDefinitionMap = new HashMap<String, String>();
            for (Map.Entry<String, String> entry : filterChainDefinitions.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                key = key.substring(key.indexOf("[") + 1, key.lastIndexOf("]"));
                filterChainDefinitionMap.put(key, value);
            }
        }
        securityFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return securityFilterFactoryBean;
    }

    @Bean(name = "filterSecurityFilterRegistrationBean")
    @ConditionalOnMissingBean
    protected FilterRegistrationBean<Filter> filterSecurityFilterRegistrationBean() throws Exception {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD,
                DispatcherType.INCLUDE, DispatcherType.ERROR);
        filterRegistrationBean.setFilter((AbstractShiroFilter) rabbitSecurityFilterFactoryBean().getObject());
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}

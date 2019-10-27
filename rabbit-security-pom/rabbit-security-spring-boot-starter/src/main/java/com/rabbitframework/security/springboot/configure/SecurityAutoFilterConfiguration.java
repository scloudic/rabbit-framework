package com.rabbitframework.security.springboot.configure;

import com.rabbitframework.security.spring.web.SecurityFilterFactoryBean;
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
    protected SecurityFilterFactoryBean rabbitSecurityFilterFactoryBean() {
        SecurityFilterFactoryBean securityFilterFactoryBean = new SecurityFilterFactoryBean();
        securityFilterFactoryBean.setSecurityManager(securityManager);
        securityFilterFactoryBean.setLoginUrl(rabbitSecurityProperties.getLoginUrl());
        securityFilterFactoryBean.setUnauthorizedUrl(rabbitSecurityProperties.getUnauthorizedUrl());
        securityFilterFactoryBean.setFilterUrls(rabbitSecurityProperties.getFilterUrls());
        securityFilterFactoryBean.setFilterChainDefinitionMap(rabbitSecurityProperties.getFilterChainDefinitions());
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

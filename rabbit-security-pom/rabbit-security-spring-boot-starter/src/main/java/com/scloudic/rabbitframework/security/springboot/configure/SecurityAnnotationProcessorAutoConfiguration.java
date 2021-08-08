package com.scloudic.rabbitframework.security.springboot.configure;

import com.scloudic.rabbitframework.security.spring.interceptor.SecurityAuthorizationAttributeSourceAdvisor;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class SecurityAnnotationProcessorAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }


    /**
     * 基于注解拦截
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    protected SecurityAuthorizationAttributeSourceAdvisor
    securityAuthorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        SecurityAuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
                new SecurityAuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}

package com.rabbitframework.security.springboot.configure;

import com.rabbitframework.security.spring.LifecycleBeanPostProcessor;
import com.rabbitframework.security.spring.SecurityEventBusBeanPostProcessor;
import org.apache.shiro.event.EventBus;
import org.apache.shiro.event.support.DefaultEventBus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link LifecycleBeanPostProcessor},{@link SecurityEventBusBeanPostProcessor}
 *
 * @since 3.3.1
 */
@Configuration
public class SecurityBeanAutoConfiguration {
    @Bean(name = "lifecycleBeanPostProcessor")
    @ConditionalOnMissingBean
    protected LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    protected EventBus eventBus() {
        return new DefaultEventBus();
    }

    @Bean
    protected SecurityEventBusBeanPostProcessor securityEventBusBeanPostProcessor() {
        return new SecurityEventBusBeanPostProcessor(eventBus());
    }

}

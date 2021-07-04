package com.rabbitframework.security.spring;

import org.apache.shiro.event.EventBus;
import org.apache.shiro.event.EventBusAware;
import org.apache.shiro.event.Subscribe;
import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.List;

/**
 *
 * Spring {@link BeanPostProcessor} that detects, {@link EventBusAware} and classes containing {@link Subscribe @Subscribe} methods.
 * Any classes implementing EventBusAware will have the setEventBus() method called with the <code>eventBus</code>. Any
 * classes discovered with methods that are annotated with @Subscribe will be automaticly registered with the EventBus.
 *
 * @see EventBusAware
 * @see Subscribe
 * @since 3.3.1
 */
public class SecurityEventBusBeanPostProcessor implements BeanPostProcessor {

    final private EventBus eventBus;

    public SecurityEventBusBeanPostProcessor(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof EventBusAware) {
            ((EventBusAware) bean).setEventBus(eventBus);
        } else if (isEventSubscriber(bean)) {
            eventBus.register(bean);
        }

        return bean;
    }

    private boolean isEventSubscriber(Object bean) {
        List annotatedMethods = ClassUtils.getAnnotatedMethods(bean.getClass(), Subscribe.class);
        return !CollectionUtils.isEmpty(annotatedMethods);
    }

}

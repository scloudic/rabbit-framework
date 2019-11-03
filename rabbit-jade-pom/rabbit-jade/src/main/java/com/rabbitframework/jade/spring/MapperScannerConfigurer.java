package com.rabbitframework.jade.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Map;

import static org.springframework.util.Assert.notNull;

/**
 * * Configuration sample:
 * <p>
 *
 * <pre class="code">
 * {@code
 *   <bean class="com.rabbitframework.jade.spring.MapperScannerConfigurer">
 *       <property name="basePackage" value="org.mybatis.spring.sample.mapper" />
 *       <property name="rabbitJadeFactoryBeanName" value="rabbitJadeFactory" />
 *   </bean>
 * }
 * </pre>
 */
public class MapperScannerConfigurer implements
        BeanDefinitionRegistryPostProcessor, InitializingBean,
        ApplicationContextAware, BeanNameAware {

    private String basePackage;

    private String rabbitJadeFactoryBeanName;

    private ApplicationContext applicationContext;

    private String beanName;

    private boolean processPropertyPlaceHolders;

    private BeanNameGenerator nameGenerator;

    public MapperScannerConfigurer() {

    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setRabbitJadeFactoryBeanName(String rabbitJadeFactoryBeanName) {
        this.rabbitJadeFactoryBeanName = rabbitJadeFactoryBeanName;
    }

    public void setProcessPropertyPlaceHolders(
            boolean processPropertyPlaceHolders) {
        this.processPropertyPlaceHolders = processPropertyPlaceHolders;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setBeanName(String name) {
        this.beanName = name;
    }

    public BeanNameGenerator getNameGenerator() {
        return nameGenerator;
    }

    public void setNameGenerator(BeanNameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    public void afterPropertiesSet() throws Exception {
        notNull(this.basePackage, "Property 'basePackage' is required");
    }

    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) {
        // left intentionally blank
    }

    public void postProcessBeanDefinitionRegistry(
            BeanDefinitionRegistry registry) throws BeansException {
        if (this.processPropertyPlaceHolders) {
            processPropertyPlaceHolders();
        }
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        scanner.setRabbitJadeFactoryBeanName(this.rabbitJadeFactoryBeanName);
        scanner.setResourceLoader(this.applicationContext);
        scanner.setBeanNameGenerator(this.nameGenerator);
        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage,
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    private void processPropertyPlaceHolders() {
        Map<String, PropertyResourceConfigurer> prcs = applicationContext
                .getBeansOfType(PropertyResourceConfigurer.class);

        if (!prcs.isEmpty()
                && applicationContext instanceof GenericApplicationContext) {
            BeanDefinition mapperScannerBean = ((GenericApplicationContext) applicationContext)
                    .getBeanFactory().getBeanDefinition(beanName);

            DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
            factory.registerBeanDefinition(beanName, mapperScannerBean);

            for (PropertyResourceConfigurer prc : prcs.values()) {
                prc.postProcessBeanFactory(factory);
            }

            PropertyValues values = mapperScannerBean.getPropertyValues();

            this.basePackage = updatePropertyValue("basePackage", values);
            this.rabbitJadeFactoryBeanName = updatePropertyValue(
                    "rabbitJadeFactoryBeanName", values);
        }
    }

    private String updatePropertyValue(String propertyName,
                                       PropertyValues values) {
        PropertyValue property = values.getPropertyValue(propertyName);

        if (property == null) {
            return null;
        }

        Object value = property.getValue();

        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return value.toString();
        } else if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        } else {
            return null;
        }
    }

}

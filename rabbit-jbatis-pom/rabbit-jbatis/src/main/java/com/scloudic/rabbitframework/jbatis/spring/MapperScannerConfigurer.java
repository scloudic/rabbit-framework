package com.scloudic.rabbitframework.jbatis.spring;

import static org.springframework.util.Assert.notNull;

import com.scloudic.rabbitframework.core.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Configuration sample:
 *
 *
 * <pre class="code">
 * {@code
 *   <bean class="com.scloudic.rabbitframework.jbatis.spring.MapperScannerConfigurer">
 *       <property name="basePackages" value="org.mybatis.spring.sample.mapper" />
 *       <property name="rabbitJbatisFactoryBeanName" value="rabbitJbatisFactory" />
 *   </bean>
 * }
 * </pre>
 */
public class MapperScannerConfigurer
        implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware {

    private String basePackages;

    private String rabbitJbatisFactoryBeanName;

    private ApplicationContext applicationContext;

    private BeanNameGenerator nameGenerator;

    public MapperScannerConfigurer() {

    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }

    public void setRabbitJbatisFactoryBeanName(String rabbitJbatisFactoryBeanName) {
        this.rabbitJbatisFactoryBeanName = rabbitJbatisFactoryBeanName;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public BeanNameGenerator getNameGenerator() {
        return nameGenerator;
    }

    public void setNameGenerator(BeanNameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    public void afterPropertiesSet() throws Exception {
        notNull(this.basePackages, "Property 'basePackages' is required");
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // left intentionally blank
    }

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        scanner.setRabbitJbatisFactoryBeanName(this.rabbitJbatisFactoryBeanName);
        scanner.setResourceLoader(this.applicationContext);
        scanner.setBeanNameGenerator(this.nameGenerator);
        scanner.registerFilters();
        String[] packages = StringUtils.tokenizeToStringArray(this.basePackages);
        scanner.scan(packages);
    }
}

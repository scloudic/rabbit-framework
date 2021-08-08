package com.scloudic.rabbitframework.jbatis.springboot.configure;

import java.util.Map;

import com.scloudic.rabbitframework.jbatis.spring.MapperScannerConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

@Configuration
@AutoConfigureAfter(RabbitJbatisAutoConfiguration.class)
public class MapperScannerAutoConfiguration implements ImportBeanDefinitionRegistrar {
    // private static final Logger logger =
    // LoggerFactory.getLogger(MapperScannerAutoConfiguration.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata
                .getAnnotationAttributes(MapperScan.class.getName());
        String[] basePackages = (String[]) annotationAttributes.get("basePackages");
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
        builder.addPropertyValue("basePackages", basePackages);
        builder.addPropertyValue("rabbitJbatisFactoryBeanName", "rabbitJbatisFactory");
        registry.registerBeanDefinition("rabbitJbatisMapperScannerConfigurer", builder.getRawBeanDefinition());
    }
}
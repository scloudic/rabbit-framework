package com.rabbitframework.dbase.springboot.configure;

import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import com.rabbitframework.dbase.spring.MapperScannerConfigurer;

@Configuration
@AutoConfigureAfter(RabbitDbaseAutoConfiguration.class)
public class MapperScannerAutoConfiguration implements ImportBeanDefinitionRegistrar {
	// private static final Logger logger =
	// LoggerFactory.getLogger(MapperScannerAutoConfiguration.class);

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Map<String, Object> annotationAttributes = importingClassMetadata
				.getAnnotationAttributes(MapperScan.class.getName());
		String basePackages = (String) annotationAttributes.get("basePackages");
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
		builder.addPropertyValue("basePackage", basePackages);
		builder.addPropertyValue("rabbitDbaseFactoryBeanName", "rabbitDbaseFactory");
		registry.registerBeanDefinition("rabbitDbaseMapperScannerConfigurer", builder.getRawBeanDefinition());
	}
}
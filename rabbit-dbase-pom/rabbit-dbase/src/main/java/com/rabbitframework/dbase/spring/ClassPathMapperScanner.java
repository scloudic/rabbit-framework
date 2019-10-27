package com.rabbitframework.dbase.spring;

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

import com.rabbitframework.dbase.annontations.Mapper;

public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {

	private String rabbitDbaseFactoryBeanName;

	public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
		super(registry, false);
	}

	public void setRabbitDbaseFactoryBeanName(String rabbitDbaseFactoryBeanName) {
		this.rabbitDbaseFactoryBeanName = rabbitDbaseFactoryBeanName;
	}

	/**
	 * Configures parent scanner to search for the right interfaces. It can
	 * search for all interfaces or just for those that extends a
	 * markerInterface or/and those annotated with the annotationClass
	 */
	public void registerFilters() {
		addIncludeFilter(new AnnotationTypeFilter(Mapper.class));
		// exclude package-info.java
		// addExcludeFilter(new TypeFilter() {
		// public boolean match(MetadataReader metadataReader,
		// MetadataReaderFactory metadataReaderFactory)
		// throws IOException {
		// String className = metadataReader.getClassMetadata()
		// .getClassName();
		// return className.endsWith("package-info");
		// }
		// });
		addExcludeFilter((metadataReader, metadataReaderFactory) -> {
			String className = metadataReader.getClassMetadata().getClassName();
			return className.endsWith("package-info");
		});

	}

	@Override
	public Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

		if (beanDefinitions.isEmpty()) {
			logger.warn("No Dbase mapper was found in '" + Arrays.toString(basePackages)
					+ "' package. Please check your configuration.");
		} else {
			for (BeanDefinitionHolder holder : beanDefinitions) {
				GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();

				if (logger.isDebugEnabled()) {
					logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '"
							+ definition.getBeanClassName() + "' mapperInterface");
				}

				// the mapper interface is the original class of the bean
				// but, the actual class of the bean is MapperFactoryBean
				definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());
				definition.setBeanClass(MapperFactoryBean.class);

				// definition.getPropertyValues().add("addToConfig",
				// this.addToConfig);

				boolean explicitFactoryUsed = false;
				if (StringUtils.hasText(this.rabbitDbaseFactoryBeanName)) {
					definition.getPropertyValues().add("rabbitDbaseFactory",
							new RuntimeBeanReference(this.rabbitDbaseFactoryBeanName));
					explicitFactoryUsed = true;
				}

				if (!explicitFactoryUsed) {
					if (logger.isDebugEnabled()) {
						logger.debug("Enabling autowire by type for MapperFactoryBean with name '"
								+ holder.getBeanName() + "'.");
					}
					definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
				}
			}
		}

		return beanDefinitions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
		if (super.checkCandidate(beanName, beanDefinition)) {
			return true;
		} else {
			logger.warn(
					"Skipping MapperFactoryBean with name '" + beanName + "' and '" + beanDefinition.getBeanClassName()
							+ "' mapperInterface" + ". Bean already defined with the same name!");
			return false;
		}
	}

}

package com.scloudic.rabbitframework.core.reflect.wrapper;

import com.scloudic.rabbitframework.core.reflect.MetaObject;
import com.scloudic.rabbitframework.core.reflect.factory.ObjectFactory;
import com.scloudic.rabbitframework.core.reflect.property.PropertyTokenizer;

import java.util.List;

public interface ObjectWrapper {

	Object get(PropertyTokenizer prop);

	void set(PropertyTokenizer prop, Object value);

	String findProperty(String name, boolean useCamelCaseMapping);

	String[] getGetterNames();

	String[] getSetterNames();

	Class<?> getSetterType(String name);

	Class<?> getGetterType(String name);

	boolean hasSetter(String name);

	boolean hasGetter(String name);

	MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop,
										ObjectFactory objectFactory);

	boolean isCollection();

	public void add(Object element);

	public <E> void addAll(List<E> element);

}

/*
 *    Copyright 2009-2012 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.scloudic.rabbitframework.core.reflect.wrapper;

import com.scloudic.rabbitframework.core.exceptions.ReflectionException;
import com.scloudic.rabbitframework.core.reflect.MetaClass;
import com.scloudic.rabbitframework.core.reflect.MetaObject;
import com.scloudic.rabbitframework.core.reflect.MetaObjectUtils;
import com.scloudic.rabbitframework.core.reflect.factory.ObjectFactory;
import com.scloudic.rabbitframework.core.reflect.invoker.Invoker;
import com.scloudic.rabbitframework.core.reflect.property.PropertyTokenizer;

import java.util.List;

/**
 * 对象封装Bean的实现类
 *
 * @author Justin
 */
public class BeanWrapper extends BaseWrapper {

	private Object object;
	private MetaClass metaClass;

	public BeanWrapper(MetaObject metaObject, Object object) {
		super(metaObject);
		this.object = object;
		this.metaClass = MetaClass.forClass(object.getClass());
	}

	public Object get(PropertyTokenizer prop) {
		if (prop.getIndex() != null) {
			Object collection = resolveCollection(prop, object);
			return getCollectionValue(prop, collection);
		} else {
			return getBeanProperty(prop, object);
		}
	}

	public void set(PropertyTokenizer prop, Object value) {
		if (prop.getIndex() != null) {
			Object collection = resolveCollection(prop, object);
			setCollectionValue(prop, collection, value);
		} else {
			setBeanProperty(prop, object, value);
		}
	}

	public String findProperty(String name, boolean useCamelCaseMapping) {
		return metaClass.findProperty(name, useCamelCaseMapping);
	}

	public String[] getGetterNames() {
		return metaClass.getGetterNames();
	}

	public String[] getSetterNames() {
		return metaClass.getSetterNames();
	}

	public Class<?> getSetterType(String name) {
		PropertyTokenizer prop = new PropertyTokenizer(name);
		if (prop.hasNext()) {
			MetaObject metaValue = metaObject.metaObjectForProperty(prop
					.getIndexedName());
			if (metaValue == MetaObjectUtils.NULL_META_OBJECT) {
				return metaClass.getSetterType(name);
			} else {
				return metaValue.getSetterType(prop.getChildren());
			}
		} else {
			return metaClass.getSetterType(name);
		}
	}

	public Class<?> getGetterType(String name) {
		PropertyTokenizer prop = new PropertyTokenizer(name);
		if (prop.hasNext()) {
			MetaObject metaValue = metaObject.metaObjectForProperty(prop
					.getIndexedName());
			if (metaValue == MetaObjectUtils.NULL_META_OBJECT) {
				return metaClass.getGetterType(name);
			} else {
				return metaValue.getGetterType(prop.getChildren());
			}
		} else {
			return metaClass.getGetterType(name);
		}
	}

	public boolean hasSetter(String name) {
		PropertyTokenizer prop = new PropertyTokenizer(name);
		if (prop.hasNext()) {
			if (metaClass.hasSetter(prop.getIndexedName())) {
				MetaObject metaValue = metaObject.metaObjectForProperty(prop
						.getIndexedName());
				if (metaValue == MetaObjectUtils.NULL_META_OBJECT) {
					return metaClass.hasSetter(name);
				} else {
					return metaValue.hasSetter(prop.getChildren());
				}
			} else {
				return false;
			}
		} else {
			return metaClass.hasSetter(name);
		}
	}

	public boolean hasGetter(String name) {
		PropertyTokenizer prop = new PropertyTokenizer(name);
		if (prop.hasNext()) {
			if (metaClass.hasGetter(prop.getIndexedName())) {
				MetaObject metaValue = metaObject.metaObjectForProperty(prop
						.getIndexedName());
				if (metaValue == MetaObjectUtils.NULL_META_OBJECT) {
					return metaClass.hasGetter(name);
				} else {
					return metaValue.hasGetter(prop.getChildren());
				}
			} else {
				return false;
			}
		} else {
			return metaClass.hasGetter(name);
		}
	}

	public MetaObject instantiatePropertyValue(String name,
                                               PropertyTokenizer prop, ObjectFactory objectFactory) {
		MetaObject metaValue;
		Class<?> type = getSetterType(prop.getName());
		try {
			Object newObject = objectFactory.create(type);
			metaValue = MetaObject.forObject(newObject,
					metaObject.getObjectFactory());
			set(prop, newObject);
		} catch (Exception e) {
			throw new ReflectionException("Cannot set value of property '"
					+ name + "' because '" + name
					+ "' is null and cannot be instantiated on instance of "
					+ type.getName() + ". Cause:" + e.toString(), e);
		}
		return metaValue;
	}

	private Object getBeanProperty(PropertyTokenizer prop, Object object) {
		try {
			Invoker method = metaClass.getGetInvoker(prop.getName());
			try {
				return method.invoke(object, NO_ARGUMENTS);
			} catch (Throwable t) {
				throw t;
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Throwable t) {
			throw new ReflectionException("Could not get property '"
					+ prop.getName() + "' from " + object.getClass()
					+ ".  Cause: " + t.toString(), t);
		}
	}

	private void setBeanProperty(PropertyTokenizer prop, Object object,
                                 Object value) {
		try {
			Invoker method = metaClass.getSetInvoker(prop.getName());
			Object[] params = { value };
			try {
				method.invoke(object, params);
			} catch (Throwable t) {
				throw t;
			}
		} catch (Throwable t) {
			throw new ReflectionException("Could not set property '"
					+ prop.getName() + "' of '" + object.getClass()
					+ "' with value '" + value + "' Cause: " + t.toString(), t);
		}
	}

	public boolean isCollection() {
		return false;
	}

	public void add(Object element) {
		throw new UnsupportedOperationException();
	}

	public <E> void addAll(List<E> list) {
		throw new UnsupportedOperationException();
	}

}

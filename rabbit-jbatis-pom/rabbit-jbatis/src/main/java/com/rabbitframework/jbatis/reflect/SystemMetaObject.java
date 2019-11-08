package com.rabbitframework.jbatis.reflect;

import com.tjzq.commons.reflect.factory.DefaultObjectFactory;
import com.tjzq.commons.reflect.factory.ObjectFactory;

public class SystemMetaObject {

	public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(
			NullObject.class, DEFAULT_OBJECT_FACTORY);

	private static class NullObject {
	}

	public static MetaObject forObject(Object object) {
		return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY);
	}

}

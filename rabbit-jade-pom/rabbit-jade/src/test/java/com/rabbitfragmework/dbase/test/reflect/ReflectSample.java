package com.rabbitfragmework.dbase.test.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.rabbitframework.jade.mapping.BaseMapper;
import com.tjzq.commons.utils.ReflectUtils;

public class ReflectSample {
	public static void main(String[] args) throws Exception {
		// Class clazz = getGenericMapper(TestMapper.class);
		// System.out.println(clazz);
		Method[] methods = TestMapper.class.getMethods();
		// Field[] fields = TestMapper.class.getFields();
		// System.out.println("fields:"+fields.length);
		// Method method = methods[0];
		// Class<?>[] parameterTypes = method.getParameterTypes();
		// Class<?> parameterType = parameterTypes[0];
		// System.out.println(parameterType.getSimpleName());
		// Type[] types = method.getGenericParameterTypes();
		// System.out.println(types[0].getTypeName());
		// for (Method method : methods) {
		// if (method.getName().equals("batchUpdate")) {
		// RowMapper rowMapper = RowMapperUtil.getRowMapper(method,
		// TestBean.class);
		// System.out.println(rowMapper);
		// }
		// }
		for (Method method : methods) {
			if (method.getName().equals("batchUpdate")) {
				Type[] parameters = method.getGenericParameterTypes();
				Type parameter = parameters[0];
				System.out.println(parameter);
				Type[] t = ((ParameterizedType) parameter).getActualTypeArguments();
				System.out.println(ReflectUtils.getGenericClassByType(t[0]));
			}
		}
	}

	/**
	 * 获取接口泛型，找出继承{@link BaseMapper}接口,获取对应的泛型。 业务上mapper不允许多级父类，只存在一级多实现
	 *
	 * @param clazz
	 * @return
	 */
	public static Class<?> getGenericMapper(Class<?> clazz) {
		Type[] genericInterfaces = clazz.getGenericInterfaces();
		int genericInterfacesLength = genericInterfaces.length;
		Class rawType = null;
		if (genericInterfacesLength > 0) {
			for (Type type : genericInterfaces) {
				/* 如果为真,表示mapper子类没有泛型类 */
				if (type == BaseMapper.class) {
					break;
				}

				if (type instanceof Class) {
					continue;
				}
				ParameterizedType parameterizedType = ((ParameterizedType) type);
				Type params = parameterizedType.getRawType();
				if (params == BaseMapper.class) {
					Type actualTypeArgs = parameterizedType.getActualTypeArguments()[0];
					rawType = ReflectUtils.getGenericClassByType(actualTypeArgs);
				}
			}
		}
		return rawType;
	}
}

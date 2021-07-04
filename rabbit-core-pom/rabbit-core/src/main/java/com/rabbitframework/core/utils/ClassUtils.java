package com.rabbitframework.core.utils;
import com.rabbitframework.core.exceptions.ClassException;
import com.rabbitframework.core.exceptions.NewInstanceException;
import com.rabbitframework.core.exceptions.TypeException;
import com.rabbitframework.core.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 类解析工具类
 *
 * @author Justin
 */
public class ClassUtils {
	private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);
	private static final Set<Class<?>> WRAPPER_TYPES = new HashSet<Class<?>>(Arrays.asList(Boolean.class, Byte.class,
			Character.class, Double.class, Float.class, Integer.class, Long.class, Short.class, Void.class));
	public static String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
	private static String CLASS_SUFFIX = "**/*.class";

	/**
	 * 根据class字符名称转换Class对象
	 *
	 * @param classForName
	 * @return
	 */
	public static Class<?> classForName(String classForName) {
		ClassLoader classLoader = ClassUtils.getClassLoader();
		Class<?> c = null;
		try {
			c = Class.forName(classForName, true, classLoader);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new ClassException(e);
		}
		return c;
	}

	/**
	 * 获取当前classLoader
	 *
	 * @return
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
		}
		if (cl == null) {
			cl = ClassUtils.class.getClassLoader();
		}

		if (cl == null) {
			cl = ClassLoader.getSystemClassLoader();
		}
		return cl;
	}

	/**
	 * 根据class包路径获取所有class对象
	 *
	 * @param classPackageName
	 * @return
	 */
	public static List<Class<?>> getClassNamePackage(String... classPackageName) {
		List<Class<?>> clazzs = new ArrayList<Class<?>>();
		for (String packageName : classPackageName) {
			String str = convertClassNameToResourcePath(packageName);
			str = CLASSPATH_ALL_URL_PREFIX + str + "/" + CLASS_SUFFIX;
			List<String> classNames = null;
			try {
				classNames = ResourceUtils.getClassNames(str);
				for (String className : classNames) {
					Class<?> clazz = classForName(className);
					clazzs.add(clazz);
				}
			} catch (Exception e) {
				logger.error("Could not read package: " + packageName + ":" + e.getMessage(), e);
				throw new TypeException("Could not read package: " + packageName, e);
			}

		}
		return clazzs;
	}

	public static InputStream getResourceAsStream(String name) {
		InputStream in = null;
		ClassLoader classLoader = getClassLoader();
		if (classLoader != null) {
			in = classLoader.getResourceAsStream(name);
		}
		return in;
	}

	public static String convertClassNameToResourcePath(String className) {
		return className.replace('.', '/');
	}

	public static String convertResourcePathToClassName(String resourcePath) {
		Assert.notNull(resourcePath, "Resource path must not be null");
		return resourcePath.replace('/', '.');
	}

	/**
	 * 获取泛型
	 *
	 * @param clazz
	 * @param index
	 * @return
	 */
	public static Type getTypeParameter(Class<?> clazz, int index) {
		Type genericSuperclass = clazz.getGenericSuperclass();
		if (!(genericSuperclass instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] types = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
		if (index >= types.length || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size of Parameterized Type: " + types.length);
		}
		Type rawType = types[index];
		if (rawType instanceof ParameterizedType) {
			rawType = ((ParameterizedType) rawType).getRawType();
		}
		return rawType;
	}

	public static Object newInstance(Class<?> clazz) {
		if (clazz == null) {
			String msg = "Class method parameter cannot be null.";
			throw new IllegalArgumentException(msg);
		}
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new NewInstanceException("Unable to instantiate class [" + clazz.getName() + "]", e);
		}
	}

	public static Object newInstance(String className) {
		return newInstance(classForName(className));
	}

	/**
	 * 通过反射机制进行类的实例化
	 *
	 * @param clazz
	 * @param constructorType
	 *            构造函数参数类型
	 * @param args
	 *            构造函数中的参数
	 * @return
	 */
	public static Object newInstance(Class<?> clazz, Class<?>[] constructorType, Object... args) {
		try {
			if (constructorType != null) {
				Constructor<?> constructor = clazz.getConstructor(constructorType);
				return constructor.newInstance(args);
			} else {
				return newInstance(clazz);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new NewInstanceException(e.getMessage(), e);
		}
	}

	/**
	 * 通过反射机制进行类的实例化
	 *
	 * @param className
	 *            类名
	 * @param parameterTypes
	 *            构造函数参数类型
	 * @param args
	 *            构造函数中的参数
	 * @return
	 */
	public static Object newInstance(String className, Class<?>[] parameterTypes, Object... args) {
		Class<?> clazz = classForName(className);
		try {
			if (parameterTypes != null && parameterTypes.length > 0) {
				if (args == null || args.length != parameterTypes.length) {
					logger.error("Unable to instantiate class [" + clazz.getName() + "]");
					throw new NewInstanceException("Unable to instantiate class [" + clazz.getName() + "]");
				}
				Constructor<?> constructor = clazz.getConstructor(parameterTypes);
				return constructor.newInstance(args);
			} else {
				return newInstance(clazz);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new NewInstanceException(e.getMessage(), e);
		}
	}

	public static Object newInstance(Class<?> clazz, Object... args) {
		Class<?>[] argTypes = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		return newInstance(clazz, argTypes, args);
	}

	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... argTypes) {
		try {
			return clazz.getConstructor(argTypes);
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage(), e);
			throw new IllegalStateException(e);
		}

	}

	public static Object newInstance(Constructor<?> ctor, Object... args) {
		try {
			return ctor.newInstance(args);
		} catch (Exception e) {
			String msg = "Unable to instantiate Permission instance with constructor [" + ctor + "]";
			logger.error(msg, e);
			throw new NewInstanceException(msg, e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <A, B> B cast(A a) {
		return (B) a;
	}

	public static boolean isWrapperType(Class<?> clazz) {
		return WRAPPER_TYPES.contains(clazz);
	}

	public static boolean isPrimitiveType(Class<?> clazz) {
		if (clazz == null) {
			return false;
		}
		return clazz.isPrimitive() || isWrapperType(clazz);
	}

	public static boolean isBasicType(Class<?> clazz) {
		if (clazz == null) {
			return false;
		}
		return (String.class.isAssignableFrom(clazz)) || isPrimitiveType(clazz);
	}
}

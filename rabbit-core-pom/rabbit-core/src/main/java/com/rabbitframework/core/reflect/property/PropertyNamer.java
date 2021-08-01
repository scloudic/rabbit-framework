package com.rabbitframework.core.reflect.property;
import com.rabbitframework.core.exceptions.ReflectionException;

import java.util.Locale;

/**
 * 属性名称判断类
 *
 * @author Justin
 */
public class PropertyNamer {

	public static String methodToProperty(String name) {
		if (name.startsWith("is")) {
			name = name.substring(2);
		} else if (name.startsWith("get") || name.startsWith("set")) {
			name = name.substring(3);
		} else {
			throw new ReflectionException("Error parsing property name '"
					+ name + "'.  Didn't start with 'is', 'get' or 'set'.");
		}

		if (name.length() == 1
				|| (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
			name = name.substring(0, 1).toLowerCase(Locale.ENGLISH)
					+ name.substring(1);
		}

		return name;
	}

	public static boolean isProperty(String name) {
		return name.startsWith("get") || name.startsWith("set")
				|| name.startsWith("is");
	}

	public static boolean isGetter(String name) {
		return name.startsWith("get") || name.startsWith("is");
	}

	public static boolean isSetter(String name) {
		return name.startsWith("set");
	}

}

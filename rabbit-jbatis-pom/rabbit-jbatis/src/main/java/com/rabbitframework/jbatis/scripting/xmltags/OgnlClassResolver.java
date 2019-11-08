package com.rabbitframework.jbatis.scripting.xmltags;

import java.util.HashMap;
import java.util.Map;

import com.tjzq.commons.utils.ClassUtils;

import ognl.ClassResolver;

public class OgnlClassResolver implements ClassResolver {
    private Map<String, Class<?>> classes = new HashMap<String, Class<?>>(101);

    @Override
    public Class classForName(String className, Map context) throws ClassNotFoundException {
        Class<?> result = null;
        if ((result = classes.get(className)) == null) {
            try {
                result = classForName(className);
            } catch (ClassNotFoundException el) {
                if (className.indexOf(".") == -1) {
                    result = classForName("java.lang." + className);
                    classes.put("java.lang." + className, result);
                }
            }
            classes.put(className, result);
        }
        return result;
    }

    public Class<?> classForName(String classForName) throws ClassNotFoundException {
        ClassLoader classLoader = ClassUtils.getClassLoader();
        Class<?> c = Class.forName(classForName, true, classLoader);
        return c;
    }
}

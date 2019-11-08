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
package com.rabbitframework.jbatis.intercept;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.rabbitframework.jbatis.annontations.Intercept;
import com.rabbitframework.jbatis.exceptions.PluginException;

public class Plugin implements InvocationHandler {

    private Object target;
    private Interceptor interceptor;
    private Map<Class<?>, Set<Method>> interceptMap;

    private Plugin(Object target, Interceptor interceptor,
                   Map<Class<?>, Set<Method>> interceptMap) {
        this.target = target;
        this.interceptor = interceptor;
        this.interceptMap = interceptMap;
    }

    public static Object wrap(Object target, Interceptor interceptor) {
        Map<Class<?>, Set<Method>> interceptMap = getInterceptMap(interceptor);
        Class<?> type = target.getClass();
        Class<?>[] interfaces = getAllInterfaces(type, interceptMap);
        if (interfaces.length > 0) {
            return Proxy.newProxyInstance(type.getClassLoader(), interfaces,
                    new Plugin(target, interceptor, interceptMap));
        }
        return target;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        try {
            Set<Method> methods = interceptMap.get(method.getDeclaringClass());
            if (methods != null && methods.contains(method)) {
                return interceptor.intercept(new Invocation(target, method,
                        args));
            }
            return method.invoke(target, args);
        } catch (Exception e) {
            throw e;
        }
    }

    private static Map<Class<?>, Set<Method>> getInterceptMap(
            Interceptor interceptor) {
        Intercept interceptsAnnotation = interceptor.getClass().getAnnotation(
                Intercept.class);
        if (interceptsAnnotation == null) { // issue #251
            throw new PluginException(
                    "No @Intercepts annotation was found in interceptor "
                            + interceptor.getClass().getName());
        }

        Map<Class<?>, Set<Method>> interceptMap = new HashMap<Class<?>, Set<Method>>();
        Set<Method> methods = interceptMap.get(interceptsAnnotation
                .interfaceType());
        if (methods == null) {
            methods = new HashSet<Method>();
            interceptMap.put(interceptsAnnotation.interfaceType(), methods);
        }
        try {
            Method method = interceptsAnnotation.interfaceType().getMethod(
                    interceptsAnnotation.method(), interceptsAnnotation.args());
            methods.add(method);
        } catch (NoSuchMethodException e) {
            throw new PluginException("Could not find method on "
                    + interceptsAnnotation.interfaceType() + " named "
                    + interceptsAnnotation.method() + ". Cause: " + e, e);
        }
        return interceptMap;
    }

    private static Class<?>[] getAllInterfaces(Class<?> type,
                                               Map<Class<?>, Set<Method>> interceptMap) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        while (type != null) {
            for (Class<?> c : type.getInterfaces()) {
                if (interceptMap.containsKey(c)) {
                    interfaces.add(c);
                }
            }
            type = type.getSuperclass();
        }
        return interfaces.toArray(new Class<?>[interfaces.size()]);
    }
}

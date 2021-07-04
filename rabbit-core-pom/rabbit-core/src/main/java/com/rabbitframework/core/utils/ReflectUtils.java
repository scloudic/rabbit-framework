package com.rabbitframework.core.utils;
import com.rabbitframework.core.exceptions.ReflectionException;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.Map;

/**
 * 反射公共类
 *
 * @author: justin.liang
 * @date: 16/5/14 上午12:38
 */
public class ReflectUtils {
    /**
     * 获取泛型类
     *
     * @param clazz
     * @return
     */
    public static Class<?> getGenericClass(Class<?> clazz) {
        return getGenericClass(clazz, 0);
    }

    /**
     * 获取指定的下标的泛型类
     *
     * @param clazz
     * @param index
     * @return
     */
    public static Class<?> getGenericClass(Class<?> clazz, int index) {
        return getGenericClass(clazz, null, index);
    }

    public static Class<?> getGenericClass(Class<?> clazz, Class<?> superclass, int index) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            if (superclass != null) {
                if (superclass != genericSuperclass) {
                    return getGenericClass(clazz.getSuperclass(), superclass, index);
                }
                throw new ReflectionException(clazz.getName() + "not extends " + superclass.getName());
            }
        }

        if (genericSuperclass == null) {
            return null;
        }
        Type[] params = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size of Parameterized Type: " + params.length);
        }
        Type rawType = params[index];
        return getGenericClassByType(rawType);
        // if (rawType instanceof ParameterizedType) {
        // rawType = ((ParameterizedType) rawType).getRawType();
        // }
        // return (Class<?>) rawType;
    }

    /**
     * 泛型参数{@link Type}转换为{@link Class}
     *
     * @param type
     * @return
     */
    public static Class<?> getGenericClassByType(Type type) {
        if (type instanceof ParameterizedType) { // 处理泛型类型
            return (Class<?>) ((ParameterizedType) type).getRawType();
        } else if (type instanceof TypeVariable) {
            return (Class<?>) getGenericClassByType(((TypeVariable<?>) type).getBounds()[0]); // 处理泛型擦拭对象
        } else {// class本身也是type，强制转型
            return (Class<?>) type;
        }
    }

    /**
     * 获取返回类型
     * <p/>
     * 获取泛型中的值
     *
     * @param method
     * @return
     */
    public static Class<?> getReturnType(Method method) {
        return getReturnType(method, null);
    }

    /**
     * 获取返回类型
     * <p/>
     * 获取泛型中的值
     *
     * @param method
     * @return
     */
    public static Class<?> getReturnType(Method method, Class<?> genericClass) {
        Class<?> returnType = method.getReturnType();
        if (void.class.equals(returnType)) {
            return returnType;
        } else {
            returnType = getType(returnType, method.getGenericReturnType(), genericClass);
        }
        return returnType;
    }


    private static Class<?> getType(Class<?> type, Type returnTypeParameter, Class<?> genericClass) {
        Class<?> returnType = type;
        if (Collection.class.isAssignableFrom(type)) {
            if (returnTypeParameter instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) returnTypeParameter).getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 1) {
                    returnTypeParameter = actualTypeArguments[0];
                    if (returnTypeParameter instanceof Class) {
                        returnType = (Class<?>) returnTypeParameter;
                    } else if (returnTypeParameter instanceof ParameterizedType) {
                        returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
                    } else if (returnTypeParameter instanceof GenericArrayType) {
                        Class<?> componentType = (Class<?>) ((GenericArrayType) returnTypeParameter)
                                .getGenericComponentType();
                        returnType = Array.newInstance(componentType, 0).getClass();
                    } else if ("T".equals(returnTypeParameter.getTypeName()) && genericClass != null) {
                        returnType = genericClass;
                    }
                }
            }
        } else if (Map.class.isAssignableFrom(type)) {
            if (returnTypeParameter instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) returnTypeParameter).getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 2) {
                    returnTypeParameter = actualTypeArguments[1];
                    if (returnTypeParameter instanceof Class) {
                        returnType = (Class<?>) returnTypeParameter;
                    } else if (returnTypeParameter instanceof ParameterizedType) {
                        returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
                    }
                }
            }
        } else if (Object.class.getName().equals(returnType.getName()) && genericClass != null) {
            returnType = genericClass;
        }
        return returnType;
    }

    public static Class<?> getGenericClassByField(Field field) {
        Type genericType = field.getGenericType();
        Class<?> type = field.getType();
        if (void.class.equals(type)) {
            return type;
        } else {
            type = getType(type, genericType, null);
        }
        return type;
    }

}

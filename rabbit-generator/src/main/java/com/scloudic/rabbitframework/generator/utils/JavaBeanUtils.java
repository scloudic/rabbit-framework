package com.scloudic.rabbitframework.generator.utils;

import com.scloudic.rabbitframework.generator.mapping.type.FullyQualifiedJavaType;

public class JavaBeanUtils {
    public static String getGetterMethodName(String property, FullyQualifiedJavaType fullyQualifiedJavaType) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }

        if (fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getBooleanPrimitiveInstance())) {
            sb.insert(0, "is");
        } else {
            sb.insert(0, "get");
        }

        return sb.toString();
    }
    
    public static String getSetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }

        sb.insert(0, "set");

        return sb.toString();
    }
}

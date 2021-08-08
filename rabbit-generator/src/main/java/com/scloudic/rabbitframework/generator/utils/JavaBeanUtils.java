package com.scloudic.rabbitframework.generator.utils;

import com.scloudic.rabbitframework.generator.mapping.type.FullyQualifiedJavaType;

public class JavaBeanUtils {
    /**
     * JavaBeans rules:
     * <p>
     * eMail > geteMail() firstName > getFirstName() URL > getURL() XAxis >
     * getXAxis() a > getA() B > invalid - this method assumes that this is not
     * the case. Call getValidPropertyName first. Yaxis > invalid - this method
     * assumes that this is not the case. Call getValidPropertyName first.
     *
     * @param property
     * @return the getter method name
     */
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

    /**
     * JavaBeans rules:
     * <p>
     * eMail > seteMail() firstName > setFirstName() URL > setURL() XAxis >
     * setXAxis() a > setA() B > invalid - this method assumes that this is not
     * the case. Call getValidPropertyName first. Yaxis > invalid - this method
     * assumes that this is not the case. Call getValidPropertyName first.
     *
     * @param property
     * @return the setter method name
     */
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

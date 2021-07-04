package com.rabbitframework.jbatis.dataaccess;

import com.rabbitframework.jbatis.mapping.GenerationType;
import com.rabbitframework.core.utils.StringUtils;

public class KeyGenerator {
    private GenerationType generationType;
    private String column;
    private String selectKey;
    private String property;
    private Class<?> javaType;

    public KeyGenerator() {
    }

    public KeyGenerator(GenerationType generationType, Class<?> javaType, String property, String column, String selectKey) {
        this.generationType = generationType;
        this.column = column;
        this.selectKey = selectKey;
        this.property = property;
        this.javaType = javaType;
    }

    public GenerationType getGenerationType() {
        return generationType;
    }

    public void setGenerationType(GenerationType generationType) {
        this.generationType = generationType;
    }


    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public String getColumn() {
        return column;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getSelectKey() {
        return selectKey;
    }

    public void setSelectKey(String selectKey) {
        this.selectKey = selectKey;
    }

    public boolean isIdentity() {
        return GenerationType.IDENTITY == generationType;
    }

    public boolean isSequence() {
        return StringUtils.isNotEmpty(selectKey) && (GenerationType.SEQUENCE == generationType);
    }
}

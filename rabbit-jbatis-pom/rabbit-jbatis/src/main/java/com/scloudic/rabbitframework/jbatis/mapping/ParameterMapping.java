package com.scloudic.rabbitframework.jbatis.mapping;

/**
 * 参数类型转换
 */
public class ParameterMapping {
    private String property;
    private Class<?> javaType = Object.class;

    private ParameterMapping() {

    }

    public static class Builder {
        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(String property) {
            parameterMapping.property = property;
        }

        public Builder javaType(Class<?> javaType) {
            parameterMapping.javaType = javaType;
            return this;
        }

        public ParameterMapping build() {
            return parameterMapping;
        }
    }

    public String getProperty() {
        return property;
    }

    public Class<?> getJavaType() {
        return javaType;
    }
}

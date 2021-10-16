package com.scloudic.rabbitframework.security.springboot.configure;

public class FilterProperties {
    private String name;
    private NameType nameType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NameType getNameType() {
        return nameType;
    }

    public void setNameType(NameType nameType) {
        this.nameType = nameType;
    }

    public enum NameType {
        className, beanName;
    }
}

package com.scloudic.rabbitframework.example.web.rest;

import com.scloudic.rabbitframework.web.spring.aop.FormValidBean;

import javax.validation.constraints.NotBlank;

public class Test implements FormValidBean {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

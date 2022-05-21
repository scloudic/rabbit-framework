package com.scloudic.rabbitframework.example.web.rest;

import javax.validation.constraints.NotBlank;

public class Test {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

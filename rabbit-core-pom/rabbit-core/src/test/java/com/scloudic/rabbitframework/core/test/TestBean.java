package com.scloudic.rabbitframework.core.test;

import com.alibaba.excel.annotation.ExcelProperty;

public class TestBean {
    @ExcelProperty(value = "name", index = 0)
    private String name;
    @ExcelProperty(value = "content", index = 1)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


package com.scloudic.rabbitframework.jbatis.springboot.test.model;

import java.util.Date;


public class User implements java.io.Serializable {
    private static final long serialVersionUID = 6601565142528523969L;

    private long id;

    private String testName;


    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }


}

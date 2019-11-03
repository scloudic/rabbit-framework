package com.rabbitframework.jade.springboot.test.model;

import com.rabbitframework.jade.annontations.Column;
import com.rabbitframework.jade.annontations.ID;
import com.rabbitframework.jade.annontations.Table;
import com.rabbitframework.jade.mapping.GenerationType;

import java.util.Date;

@Table
public class TestUser implements java.io.Serializable {
    private static final long serialVersionUID = 6601565142528523969L;
    @ID(keyType = GenerationType.MANUAL)
    private long id;
    @Column
    private String testName;

    @Column
    private Date age;

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

    public void setAge(Date age) {
        this.age = age;
    }

    public Date getAge() {
        return age;
    }

}
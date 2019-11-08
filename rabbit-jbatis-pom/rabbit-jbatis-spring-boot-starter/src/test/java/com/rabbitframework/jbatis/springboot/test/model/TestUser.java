package com.rabbitframework.jbatis.springboot.test.model;

import com.rabbitframework.jbatis.annontations.Column;
import com.rabbitframework.jbatis.annontations.ID;
import com.rabbitframework.jbatis.annontations.Table;
import com.rabbitframework.jbatis.mapping.GenerationType;

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

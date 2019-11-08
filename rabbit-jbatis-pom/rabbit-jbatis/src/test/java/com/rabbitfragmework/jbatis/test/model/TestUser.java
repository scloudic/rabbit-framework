package com.rabbitfragmework.jbatis.test.model;

import java.util.Date;

import com.rabbitframework.jbatis.annontations.Column;
import com.rabbitframework.jbatis.annontations.ID;
import com.rabbitframework.jbatis.annontations.Table;

@Table
public class TestUser implements java.io.Serializable {
	private static final long serialVersionUID = 6601565142528523969L;
	@ID
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

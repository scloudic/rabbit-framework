package com.rabbitframework.commons.bean;

import java.util.ArrayList;
import java.util.List;

public class DataResponse implements IData {
	private static final long serialVersionUID = 4115707000173426028L;
	private List<IData> data = new ArrayList<IData>();
	public Integer status;
	private String test;

	public void setTest(String test) {
		this.test = test;
	}

	public String getTest() {
		return test;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public List<IData> getData() {
		return data;
	}
}

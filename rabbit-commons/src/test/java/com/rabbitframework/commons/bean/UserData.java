package com.rabbitframework.commons.bean;

public class UserData implements IData {
	private String userName;

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}

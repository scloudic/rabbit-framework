package com.rabbitframework.jade.mapping.param;

import java.util.List;

/**
 * 标准条件规范
 * 
 */
public class Criterion {
	private String condition;

	private Object value;

	private Object secondValue;

	private boolean noValue;

	private boolean singleValue;

	private boolean betweenValue;

	private boolean listValue;

	public String getCondition() {
		return condition;
	}

	public Object getValue() {
		return value;
	}

	public Object getSecondValue() {
		return secondValue;
	}

	public boolean isNoValue() {
		return noValue;
	}

	public boolean isSingleValue() {
		return singleValue;
	}

	public boolean isBetweenValue() {
		return betweenValue;
	}

	public boolean isListValue() {
		return listValue;
	}

	protected Criterion(String condition) {
		super();
		this.condition = condition;
		this.noValue = true;
	}

	protected Criterion(String condition, Object value) {
		super();
		this.condition = condition;
		this.value = value;
		if (value instanceof List<?>) {
			this.listValue = true;
		} else {
			this.singleValue = true;
		}
	}

	protected Criterion(String condition, Object value, Object secondValue) {
		super();
		this.condition = condition;
		this.value = value;
		this.secondValue = secondValue;
		this.betweenValue = true;
	}
}

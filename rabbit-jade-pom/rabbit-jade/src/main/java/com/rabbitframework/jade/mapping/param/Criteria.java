package com.rabbitframework.jade.mapping.param;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库条件拼接类
 * 
 * @author leung
 * 
 */
public class Criteria {
	protected List<Criterion> criteria;

	protected Criteria() {
		criteria = new ArrayList<Criterion>();
	}

	public boolean isValid() {
		return criteria.size() > 0;
	}

	public List<Criterion> getCriteria() {
		return criteria;
	}

	protected void addCriterion(String condition) {
		criteria.add(new Criterion(condition));
	}

	protected void addCriterion(String condition, Object value) {
		criteria.add(new Criterion(condition, value));
	}

	protected void addCriterion(String condition, Object value1, Object value2) {
		criteria.add(new Criterion(condition, value1, value2));
	}

	protected boolean valueIsNotNullOrEmpty(Object value) {
		if (value instanceof String) {
			if (value != null && !"".equals(value.toString().trim())) {
				return true;
			}
		} else if (value instanceof List<?>) {
			if (((List<?>) value).size() > 0) {
				return true;
			}
		} else if (value != null) {
			return true;
		}
		throw new NullPointerException("params value is null");
	}

	/**
	 * 传入数据库字段,生成字段为空的条件语句 如:usr_id is null
	 * 
	 * @param field
	 * @return
	 */
	public Criteria andFieldIsNull(String fieldName) {
		addCriterion(fieldName + " is null");
		return this;
	}

	/**
	 * 传入数据库字段，生成字段不为空的条件语句 如:user_id is not null
	 * 
	 * @param field
	 * @return
	 */
	public Criteria andFieldIsNotNull(String fieldName) {
		addCriterion(fieldName + " is not null");
		return this;
	}

	public Criteria andFieldIsEqualTo(String fieldName, Object value) {
		if (valueIsNotNullOrEmpty(value))
			addCriterion(fieldName + " =", value);
		return this;
	}

	public Criteria andFieldNotEqualTo(String fieldName, Object value) {
		if (valueIsNotNullOrEmpty(value))
			addCriterion(fieldName + " <>", value);
		return this;
	}

	public Criteria andFieldGreaterThan(String fieldName, Object value) {
		if (valueIsNotNullOrEmpty(value))
			addCriterion(fieldName + " >", value);
		return this;
	}

	public Criteria andFieldGreaterThanOrEqualTo(String fieldName, Object value) {
		if (valueIsNotNullOrEmpty(value))
			addCriterion(fieldName + " >=", value);
		return this;
	}

	public Criteria andFieldLessThan(String fieldName, Object value) {
		if (valueIsNotNullOrEmpty(value))
			addCriterion(fieldName + " <", value);
		return this;
	}

	public Criteria andFieldLessThanOrEqualTo(String fieldName, Object value) {
		if (valueIsNotNullOrEmpty(value))
			addCriterion(fieldName + " <=", value);
		return this;
	}

	public <T> Criteria andFieldIdIn(String fieldName, List<T> values) {
		if (valueIsNotNullOrEmpty(values))
			addCriterion(fieldName + " in", values);
		return this;
	}

	public <T> Criteria andFieldNotIn(String fieldName, List<T> values) {
		if (valueIsNotNullOrEmpty(values))
			addCriterion(fieldName + " not in", values);
		return this;
	}

	public Criteria andFieldBetween(String fieldName, Object value1, Object value2) {
		if (valueIsNotNullOrEmpty(value1) && valueIsNotNullOrEmpty(value1)) {
			addCriterion(fieldName + " between", value1, value2);
		}

		return this;
	}

	public Criteria andFieldNotBetween(String fieldName, Object value1, Object value2) {
		if (valueIsNotNullOrEmpty(value1) && valueIsNotNullOrEmpty(value1)) {
			addCriterion(fieldName + " not between", value1, value2);
		}
		return (Criteria) this;
	}

	public Criteria andDefineCondition(String fieldName, String condition, Object value) {
		if (valueIsNotNullOrEmpty(value))
			addCriterion(fieldName + " " + condition + " ", value);
		return this;
	}
}
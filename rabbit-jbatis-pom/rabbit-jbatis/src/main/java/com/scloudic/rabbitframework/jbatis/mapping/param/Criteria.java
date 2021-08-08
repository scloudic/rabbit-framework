package com.scloudic.rabbitframework.jbatis.mapping.param;

import com.scloudic.rabbitframework.jbatis.mapping.lambda.SFunction;
import com.scloudic.rabbitframework.jbatis.mapping.lambda.SFunctionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库条件拼接类
 *
 * @author leung
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


    public <T> Criteria andIsNull(SFunction<T, ?> fn) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        addCriterion(" and " + fieldName + " is null");
        return this;
    }


    public <T> Criteria orIsNull(SFunction<T, ?> fn) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        addCriterion(" or " + fieldName + " is null");
        return this;
    }


    public <T> Criteria andIsNotNull(SFunction<T, ?> fn) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        addCriterion(" and " + fieldName + " is not null");
        return this;
    }

    public <T> Criteria orIsNotNull(SFunction<T, ?> fn) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        addCriterion(" or " + fieldName + " is not null");
        return this;
    }


    public <T> Criteria andEqual(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " =", value);
        return this;
    }


    public <T> Criteria orEqual(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " =", value);
        return this;
    }

    public <T> Criteria andNotEqual(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " <>", value);
        return this;
    }

    public <T> Criteria orNotEqual(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " <>", value);
        return this;
    }

    public <T> Criteria andGreaterThan(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " >", value);
        return this;
    }

    public <T> Criteria orGreaterThan(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " >", value);
        return this;
    }

    public <T> Criteria andGreaterThanEqual(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " >=", value);
        return this;
    }

    public <T> Criteria orGreaterThanEqual(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " >=", value);
        return this;
    }

    public <T> Criteria andLessThan(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " <", value);
        return this;
    }

    public <T> Criteria orLessThan(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " <", value);
        return this;
    }


    public <T> Criteria andLessThanEqual(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " <=", value);
        return this;
    }

    public <T> Criteria orLessThanEqual(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " <=", value);
        return this;
    }

    public <T> Criteria andIn(SFunction<T, ?> fn, List<?> values) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(values))
            addCriterion(" and " + fieldName + " in", values);
        return this;
    }

    public <T> Criteria orIn(SFunction<T, ?> fn, List<?> values) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(values))
            addCriterion(" or " + fieldName + " in", values);
        return this;
    }

    public <T> Criteria andNotIn(SFunction<T, ?> fn, List<?> values) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(values))
            addCriterion(" and " + fieldName + " not in", values);
        return this;
    }

    public <T> Criteria orNotIn(SFunction<T, ?> fn, List<?> values) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(values))
            addCriterion(" or " + fieldName + " not in", values);
        return this;
    }

    public <T> Criteria andBetween(SFunction<T, ?> fn, Object value1, Object value2) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value1) && valueIsNotNullOrEmpty(value2)) {
            addCriterion(" and " + fieldName + " between", value1, value2);
        }
        return this;
    }

    public <T> Criteria orBetween(SFunction<T, ?> fn, Object value1, Object value2) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value1) && valueIsNotNullOrEmpty(value2)) {
            addCriterion(" or " + fieldName + " between", value1, value2);
        }
        return this;
    }

    public <T> Criteria andNotBetween(SFunction<T, ?> fn, Object value1, Object value2) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value1) && valueIsNotNullOrEmpty(value2)) {
            addCriterion(" and " + fieldName + " not between", value1, value2);
        }
        return (Criteria) this;
    }

    public <T> Criteria orNotBetween(SFunction<T, ?> fn, Object value1, Object value2) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value1) && valueIsNotNullOrEmpty(value2)) {
            addCriterion(" or " + fieldName + " not between", value1, value2);
        }
        return (Criteria) this;
    }

    public <T> Criteria andDefine(SFunction<T, ?> fn, String condition, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " " + condition + " ", value);
        return this;
    }

    public <T> Criteria orDefine(SFunction<T, ?> fn, String condition, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " " + condition + " ", value);
        return this;
    }

    public <T> Criteria andLike(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " like ", value);
        return this;
    }

    public <T> Criteria orLike(SFunction<T, ?> fn, Object value) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " like ", value);
        return this;
    }

    public Criteria andIsNull(String fieldName) {
        addCriterion(" and " + fieldName + " is null");
        return this;
    }

    public Criteria orIsNull(String fieldName) {
        addCriterion(" or " + fieldName + " is null");
        return this;
    }

    public Criteria andIsNotNull(String fieldName) {
        addCriterion(" and " + fieldName + " is not null");
        return this;
    }

    public Criteria orIsNotNull(String fieldName) {
        addCriterion(" or " + fieldName + " is not null");
        return this;
    }

    public Criteria andEqual(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " =", value);
        return this;
    }

    public Criteria orEqual(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " =", value);
        return this;
    }

    public Criteria andNotEqual(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " <>", value);
        return this;
    }

    public Criteria orNotEqual(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " <>", value);
        return this;
    }

    public Criteria andGreaterThan(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " >", value);
        return this;
    }

    public Criteria orGreaterThan(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " >", value);
        return this;
    }

    public Criteria andGreaterThanEqual(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " >=", value);
        return this;
    }

    public Criteria orGreaterThanEqual(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " >=", value);
        return this;
    }

    public Criteria andLessThan(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " <", value);
        return this;
    }

    public Criteria orLessThan(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " <", value);
        return this;
    }

    public Criteria andLessThanEqual(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " <=", value);
        return this;
    }
    
    public Criteria orLessThanEqual(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " <=", value);
        return this;
    }

    public Criteria andIn(String fieldName, List<?> values) {
        if (valueIsNotNullOrEmpty(values))
            addCriterion(" and " + fieldName + " in", values);
        return this;
    }

    public Criteria orIn(String fieldName, List<?> values) {
        if (valueIsNotNullOrEmpty(values))
            addCriterion(" or " + fieldName + " in", values);
        return this;
    }

    public Criteria andNotIn(String fieldName, List<?> values) {
        if (valueIsNotNullOrEmpty(values))
            addCriterion(" and " + fieldName + " not in", values);
        return this;
    }

    public Criteria orNotIn(String fieldName, List<?> values) {
        if (valueIsNotNullOrEmpty(values))
            addCriterion(" or " + fieldName + " not in", values);
        return this;
    }

    public Criteria andBetween(String fieldName, Object value1, Object value2) {
        if (valueIsNotNullOrEmpty(value1) && valueIsNotNullOrEmpty(value2)) {
            addCriterion(" and " + fieldName + " between", value1, value2);
        }
        return this;
    }

    public Criteria orBetween(String fieldName, Object value1, Object value2) {
        if (valueIsNotNullOrEmpty(value1) && valueIsNotNullOrEmpty(value2)) {
            addCriterion(" or " + fieldName + " between", value1, value2);
        }
        return this;
    }

    public Criteria andNotBetween(String fieldName, Object value1, Object value2) {
        if (valueIsNotNullOrEmpty(value1) && valueIsNotNullOrEmpty(value2)) {
            addCriterion(" and " + fieldName + " not between", value1, value2);
        }
        return (Criteria) this;
    }

    public Criteria orNotBetween(String fieldName, Object value1, Object value2) {
        if (valueIsNotNullOrEmpty(value1) && valueIsNotNullOrEmpty(value2)) {
            addCriterion(" or " + fieldName + " not between", value1, value2);
        }
        return (Criteria) this;
    }

    public Criteria andDefine(String fieldName, String condition, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " " + condition + " ", value);
        return this;
    }

    public Criteria orDefine(String fieldName, String condition, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " " + condition + " ", value);
        return this;
    }

    public Criteria andLike(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" and " + fieldName + " like ", value);
        return this;
    }

    public Criteria orLike(String fieldName, Object value) {
        if (valueIsNotNullOrEmpty(value))
            addCriterion(" or " + fieldName + " like ", value);
        return this;
    }
}

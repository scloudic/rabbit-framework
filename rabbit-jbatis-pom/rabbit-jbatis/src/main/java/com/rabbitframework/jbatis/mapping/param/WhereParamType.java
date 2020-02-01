package com.rabbitframework.jbatis.mapping.param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rabbitframework.jbatis.reflect.MetaObject;
import com.rabbitframework.jbatis.reflect.SystemMetaObject;
import com.tjzq.commons.utils.StringUtils;

/**
 * 数据库参数组合实例类 提供对数据库条件解析的接口 oredCriteria条件集体合，一般情况下使用createCriteria()方法
 * 特殊情况下，调用createCriteria(criteria)或addCreateCriteria()方法
 */
public class WhereParamType {

    protected List<Criteria> oredCriteria;
    protected Map<String, Object> params;
    protected String orderby = null;
    protected String defineCondition = null;

    public WhereParamType() {
        oredCriteria = new ArrayList<Criteria>();
        params = new HashMap<String, Object>();
    }

    public boolean isDefCondition() {
        return StringUtils.isNotBlank(defineCondition);
    }

    public boolean isParamsValid() {
        return params.size() > 0;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        if (params != null && params.size() > 0) {
            this.params.putAll(params);
        }
    }

    public void put(String key, Object value) {
        params.put(key, value);
    }

    /**
     * 根据参数，增加条件集体合
     *
     * @param criteria
     */
    public void createCriteria(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * 实例化条件类，增加条件集合
     *
     * @return
     */
    public Criteria addCreateCriteria() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * 限定条件集体合，size==1
     *
     * @return
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
    }

    /**
     * 默认为desc排序
     *
     * @param orderby
     */
    public void setOrderby(String orderby) {
        setOrderby(orderby, OrderByType.DESC);
    }

    public void setOrderby(String orderby, OrderByType type) {
        switch (type) {
            case ASC:
                this.orderby = orderby + " asc";
                break;
            default:
                this.orderby = orderby + " desc";
                break;
        }
    }

    public String getOrderby() {
        return orderby;
    }

    public void setDefineCondition(String defineCondition) {
        this.defineCondition = defineCondition;
    }

    public String getDefineCondition() {
        return defineCondition;
    }

    public static enum OrderByType {
        ASC, DESC;
    }

    /**
     * 将对象生成map参数
     *
     * @param value
     */
    public void generateParams(Object value) {
        if (value == null) {
            throw new NullPointerException("object is null");
        }
        MetaObject metaObject = SystemMetaObject.forObject(value);
        String[] getterNames = metaObject.getGetterNames();
        for (String getterName : getterNames) {
            params.put(getterName, metaObject.getValue(getterName));
        }
    }
}
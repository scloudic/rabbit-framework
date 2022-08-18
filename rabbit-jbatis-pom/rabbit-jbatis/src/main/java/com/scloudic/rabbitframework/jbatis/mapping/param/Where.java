package com.scloudic.rabbitframework.jbatis.mapping.param;

import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.jbatis.mapping.lambda.SFunction;
import com.scloudic.rabbitframework.jbatis.mapping.lambda.SFunctionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组成数据库sql条件、动态sql
 *
 * @since 3.3
 */
public class Where {
    private static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";
    protected List<Criteria> oredCriteria;
    protected Map<String, Object> params;
    protected String orderBy = null;
    protected String defineCondition = null;
    protected String groupBy = null;
    protected String having = null;
    protected String showColumns = "*";
    protected String tableSuffix = "";

    public Where() {
        oredCriteria = new ArrayList<Criteria>();
        params = new HashMap<String, Object>();
        showColumns = "*";
    }

    public Where(String showColumns) {
        oredCriteria = new ArrayList<Criteria>();
        params = new HashMap<String, Object>();
        this.showColumns = showColumns;
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

    public <T> void putProperty(SFunction<T, ?> fn, Object value) {
        put(fn, value, false);
    }

    public <T> void put(SFunction<T, ?> fn, Object value, boolean isTableField) {
        String key = "";
        if (isTableField) {
            key = SFunctionUtils.getFieldName(fn);
        } else {
            key = SFunctionUtils.getFieldPropertyName(fn);
        }
        params.put(key, value);
    }

    /**
     * 根据参数，增加条件集体合
     *
     * @param criteria criteria
     */
    public void createCriteria(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * 实例化条件类，增加条件集合
     *
     * @return Criteria
     */
    public Criteria addCreateCriteria() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * 限定条件集体合，size==1
     *
     * @return Criteria
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
        params.clear();
        showColumns = "*";
        orderBy = null;
        defineCondition = null;
        groupBy = null;
        having = null;
        tableSuffix = "";
    }


    public <T> void order(boolean condition, SFunction<T, ?> fn, boolean isAsc) {
        if (!condition) {
            return;
        }
        if (isAsc) {
            orderByAsc(fn);
        } else {
            orderByDesc(fn);
        }
    }

    public void order(boolean condition, String orderBy, boolean isAsc) {
        if (!condition) {
            return;
        }
        if (isAsc) {
            orderByAsc(orderBy);
        } else {
            orderByDesc(orderBy);
        }
    }

    public void orderByDesc(boolean condition, String orderBy) {
        if (!condition) {
            return;
        }
        orderByDesc(orderBy);
    }


    public void orderByAsc(boolean condition, String orderBy) {
        if (!condition) {
            return;
        }
        orderByAsc(orderBy);
    }

    public void orderByDesc(String orderBy) {
        setOrderBy(orderBy, OrderByType.DESC);
    }

    public <T> void orderByDesc(SFunction<T, ?> fn) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        setOrderBy(fieldName, OrderByType.DESC);
    }

    public <T> void orderByAsc(SFunction<T, ?> fn) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        setOrderBy(fieldName, OrderByType.ASC);
    }

    public void orderByAsc(String orderBy) {
        setOrderBy(orderBy, OrderByType.ASC);
    }

    /**
     * 传入orderBy字段,如果orderByType为空，默认为desc排序
     *
     * @param orderBy     orderBy
     * @param orderByType orderByType
     */
    private void setOrderBy(String orderBy, OrderByType orderByType) {
        if (StringUtils.isBlank(orderBy)) {
            return;
        }
        if (!orderBy.matches(SQL_PATTERN)) {
            return;
        }
        if (orderByType == null) {
            orderByType = OrderByType.DESC;
        }
        switch (orderByType) {
            case ASC:
                this.orderBy = orderBy + " asc";
                break;
            default:
                this.orderBy = orderBy + " desc";
                break;
        }
    }

    public String getOrderBy() {
        return orderBy;
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

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getHaving() {
        return having;
    }

    public void setHaving(String having) {
        this.having = having;
    }

    public String getShowColumns() {
        return showColumns;
    }

    public void setShowColumns(String showColumns) {
        this.showColumns = showColumns;
    }

    public String getTableSuffix() {
        return tableSuffix;
    }

    public void setTableSuffix(String tableSuffix) {
        if (StringUtils.isBlank(tableSuffix)) {
            tableSuffix = "";
        }
        this.tableSuffix = tableSuffix;
    }
}

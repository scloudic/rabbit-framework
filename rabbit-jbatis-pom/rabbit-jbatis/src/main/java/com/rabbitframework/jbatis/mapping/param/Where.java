package com.rabbitframework.jbatis.mapping.param;
import com.rabbitframework.jbatis.mapping.lambda.SFunction;
import com.rabbitframework.jbatis.mapping.lambda.SFunctionUtils;
import com.rabbitframework.core.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于数据库统一条件组成,底层使用该类拼装成sql脚本
 *
 * @since 3.3
 */
public class Where {
    protected List<Criteria> oredCriteria;
    protected Map<String, Object> params;
    protected String orderBy = null;
    protected String defineCondition = null;
    protected String showColumns = "*";

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
        params.clear();
        showColumns = "*";
        orderBy = null;
        defineCondition = null;
    }

    /**
     * 传入orderBy字段,默认为desc排序
     *
     * @param orderBy
     */
    public void setOrderBy(String orderBy) {
        setOrderBy(orderBy, OrderByType.DESC);
    }

    public <T> void setOrderBy(SFunction<T, ?> fn) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        setOrderBy(fieldName, OrderByType.DESC);
    }

    public <T> void setOrderBy(SFunction<T, ?> fn, OrderByType orderByType) {
        String fieldName = SFunctionUtils.getFieldName(fn);
        setOrderBy(fieldName, orderByType);
    }

    /**
     * 传入orderBy字段,如果orderByType为空，默认为desc排序
     *
     * @param orderBy
     * @param orderByType
     */
    public void setOrderBy(String orderBy, OrderByType orderByType) {
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

    public String getShowColumns() {
        return showColumns;
    }

    public void setShowColumns(String showColumns) {
        this.showColumns = showColumns;
    }
}

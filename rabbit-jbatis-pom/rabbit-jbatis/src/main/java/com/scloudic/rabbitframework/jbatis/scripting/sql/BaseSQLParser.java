package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.dataaccess.KeyGenerator;
import com.scloudic.rabbitframework.jbatis.mapping.EntityMap;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;
import com.scloudic.rabbitframework.jbatis.mapping.binding.EntityRegistry;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSQLParser {
    protected String sqlScript;
    protected Configuration configuration;
    protected Class<?> genericClass;
    protected Class<?> paramType;
    protected boolean batchUpdate = false;

    /**
     * 获取where查询xml脚本
     *
     * @return string
     */
    public String getSearchSql() {
        return "<where>" + "<foreach collection=\"oredCriteria\" item=\"criteria\" separator=\" and \" >"
                + "<if test=\"criteria.valid\" >" + "<trim prefix=\"(\" suffix=\")\" prefixOverrides=\"and|or\" >"
                + "<foreach collection=\"criteria.criteria\" item=\"criterion\" >" + "<choose>"
                + "<when test=\"criterion.noValue\" >" + " ${criterion.condition}" + "</when>"
                + "<when test=\"criterion.singleValue\" >" + "${criterion.condition} #{criterion.value}"
                + "</when>" + "<when test=\"criterion.betweenValue\" >"
                + " ${criterion.condition} #{criterion.value} and #{criterion.secondValue}" + "</when>"
                + "<when test=\"criterion.listValue\" >" + " ${criterion.condition}"
                + "<foreach collection=\"criterion.value\" item=\"listItem\" open=\"(\" close=\")\" separator=\",\" >"
                + "#{listItem}" + "</foreach>" + "</when>" + "</choose>" + "</foreach>" + "</trim>" + "</if>"
                + "</foreach>" + "</where>";
    }


    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setSqlScript(String sqlScript) {
        this.sqlScript = sqlScript;
    }

    /**
     * 获取当前sql
     *
     * @return
     */
    public List<KeyGenerator> getKeyGenerators() {
        return new ArrayList<>();
    }

    public Class<?> getGenericClass() {
        return genericClass;
    }

    public void setGenericClass(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    public boolean isBatchUpdate() {
        return batchUpdate;
    }

    public void setBatchUpdate(boolean batchUpdate) {
        this.batchUpdate = batchUpdate;
    }

    public Class<?> getParamType() {
        return paramType;
    }

    public void setParamType(Class<?> paramType) {
        this.paramType = paramType;
    }
    
    public RowMapper rowMapper() {
        return null;
    }

    protected EntityMap getEntityMap(String name) {
        EntityRegistry entityRegistry = configuration.getEntityRegistry();
        boolean isEntity = entityRegistry.hasEntityMap(name);
        if (!isEntity) {
            throw new NullPointerException("entity not include,the entity name is:" + name);
        }
        EntityMap entityMap = entityRegistry.getEntityMap(name);
        return entityMap;
    }

    public abstract String parserSQL();

    public abstract SqlCommendType getSqlCommendType();
}

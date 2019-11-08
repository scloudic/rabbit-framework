package com.rabbitframework.jbatis.mapping.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public abstract class AbstractCollectionRowMapper implements RowMapper {
    private Class<?> elementType;

    public AbstractCollectionRowMapper(Class<?> elementType) {
        this.elementType = elementType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        int columnSize = rs.getMetaData().getColumnCount();
        Collection collection = createCollection(columnSize);
        // columnIndex从1开始
        for (int columnIndex = 1; columnIndex <= columnSize; columnIndex++) {
            collection.add(JdbcUtils.getResultSetValue(rs, columnIndex, elementType));
        }
        return collection;
    }

    /**
     * 由子类覆盖此方法，提供一个空的具体集合实现类
     *
     * @param columnSize
     * @return
     */
    @SuppressWarnings("unchecked")
    protected abstract Collection createCollection(int columnSize);
}

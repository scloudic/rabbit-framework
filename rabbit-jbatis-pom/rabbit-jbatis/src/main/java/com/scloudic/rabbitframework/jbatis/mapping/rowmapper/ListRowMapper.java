package com.scloudic.rabbitframework.jbatis.mapping.rowmapper;


import java.util.ArrayList;
import java.util.Collection;

public class ListRowMapper extends AbstractCollectionRowMapper {
    public ListRowMapper(Class<?> elementType) {
        super(elementType);
    }

    @Override
    protected Collection createCollection(int columnSize) {
        return new ArrayList(columnSize);
    }
}

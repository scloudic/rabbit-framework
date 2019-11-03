package com.rabbitframework.jade.mapping.rowmapper;


import java.util.Collection;
import java.util.HashSet;

public class SetRowMapper extends AbstractCollectionRowMapper {
    public SetRowMapper(Class<?> elementType) {
        super(elementType);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Collection createCollection(int columnSize) {
        return new HashSet(columnSize * 2);
    }
}

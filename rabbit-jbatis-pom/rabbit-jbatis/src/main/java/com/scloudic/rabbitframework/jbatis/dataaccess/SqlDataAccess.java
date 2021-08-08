package com.scloudic.rabbitframework.jbatis.dataaccess;

import java.util.List;
import java.util.Map;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;

public interface SqlDataAccess {

    <T> T selectOne(String statement);


    <T> T selectOne(String statement, Object parameter);


    <E> List<E> selectList(String statement);


    <E> List<E> selectList(String statement, Object parameter);

    <E> List<E> selectList(String statement, Object parameter,
                           RowBounds rowBounds);

    <K, V> Map<K, V> selectMap(String statement, String mapKey);

    <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey);

    <K, V> Map<K, V> selectMap(String statement, Object parameter,
                               String mapKey, RowBounds rowBounds);

    int insert(String statement);


    int insert(String statement, Object parameter);

    int update(String statement);


    int update(String statement, Object parameter);


    int batchUpdate(String statement, List<Object> parameter);


    int delete(String statement);


    int delete(String statement, Object parameter);


    int create(String statement);


    <T> T getMapper(Class<T> type);

    
    Configuration getConfiguration();
}

package com.scloudic.rabbitframework.jbatis.dataaccess;

import java.util.List;
import java.util.Map;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;

public interface SqlDataAccess {

    <T> T selectOne(String statement, String dynamicSQL);


    <T> T selectOne(String statement, Object parameter, String dynamicSQL);


    <E> List<E> selectList(String statement, String dynamicSQL);


    <E> List<E> selectList(String statement, Object parameter, String dynamicSQL);

    <E> List<E> selectList(String statement, Object parameter,
                           RowBounds rowBounds, String dynamicSQL);

    <K, V> Map<K, V> selectMap(String statement, String mapKey, String dynamicSQL);

    <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, String dynamicSQL);

    <K, V> Map<K, V> selectMap(String statement, Object parameter,
                               String mapKey, RowBounds rowBounds, String dynamicSQL);

    int insert(String dynamicSQL, String statement);


    int insert(String dynamicSQL, String statement, Object parameter);

    int update(String dynamicSQL, String statement);


    int update(String dynamicSQL, String statement, Object parameter);


    int batchUpdate(String dynamicSQL, String statement, List<Object> parameter);


    int delete(String dynamicSQL, String statement);


    int delete(String dynamicSQL, String statement, Object parameter);


    int create(String dynamicSQL, String statement);


    <T> T getMapper(Class<T> type);


    Configuration getConfiguration();
}

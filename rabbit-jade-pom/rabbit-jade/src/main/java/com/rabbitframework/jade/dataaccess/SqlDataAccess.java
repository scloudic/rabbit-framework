package com.rabbitframework.jade.dataaccess;

import java.util.List;
import java.util.Map;

import com.rabbitframework.jade.builder.Configuration;
import com.rabbitframework.jade.mapping.RowBounds;

public interface SqlDataAccess {
    /**
     * 查询一条记录
     *
     * @param statement
     * @return
     */
    <T> T selectOne(String statement);

    /**
     * 根据参数查询，返回一条记录
     *
     * @param statement
     * @param parameter
     * @return
     */
    <T> T selectOne(String statement, Object parameter);

    /**
     * 查询，返回集合
     *
     * @param statement
     * @return
     */
    <E> List<E> selectList(String statement);

    /**
     * 根据参数查询，返回集合
     *
     * @param statement
     * @param parameter
     * @return
     */
    <E> List<E> selectList(String statement, Object parameter);

    <E> List<E> selectList(String statement, Object parameter,
                           RowBounds rowBounds);

    <K, V> Map<K, V> selectMap(String statement, String mapKey);

    <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey);

    <K, V> Map<K, V> selectMap(String statement, Object parameter,
                               String mapKey, RowBounds rowBounds);

    /**
     * 插入
     *
     * @param statement
     * @return
     */
    int insert(String statement);

    /**
     * 根据参数插入
     *
     * @param statement
     * @param parameter
     * @return
     */
    int insert(String statement, Object parameter);

    /**
     * 修改
     *
     * @param statement
     * @return
     */
    int update(String statement);

    /**
     * 根据参数修改
     *
     * @param statement
     * @param parameter
     * @return
     */
    int update(String statement, Object parameter);

    /**
     * 批量插入
     *
     * @param ms
     * @param parameter
     * @return
     */
    int batchUpdate(String statement, List<Object> parameter);

    /**
     * 删除
     *
     * @param statement
     * @return
     */
    int delete(String statement);

    /**
     * 根据参数删除
     *
     * @param statement
     * @param parameter
     * @return
     */
    int delete(String statement, Object parameter);

    /**
     * 创建
     *
     * @param statement
     * @return
     */
    int create(String statement);

    /**
     * 获取mapper对象
     *
     * @param type
     * @return
     */
    <T> T getMapper(Class<T> type);

    /**
     * 获取框架{@link Configuration}对象
     *
     * @return
     */
    Configuration getConfiguration();
}

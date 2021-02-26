package com.rabbitframework.jbatis.service;

import com.rabbitframework.jbatis.annontations.Update;
import com.rabbitframework.jbatis.mapping.RowBounds;
import com.rabbitframework.jbatis.mapping.param.WhereParamType;

import java.io.Serializable;
import java.util.List;

/**
 * 服务层通用接口类
 *
 * @param <T>
 */
public interface IService<T> {
    /**
     * 插入一条记录
     *
     * @param entity
     * @return
     */
    Integer insertByEntity(T entity);

    /**
     * 根据主键删除一条记录
     *
     * @param id
     * @return
     */
    Integer deleteById(Serializable id);

    /**
     * 根据参数条件{@link WhereParamType }删除数据
     *
     * @param paramType {@link WhereParamType}
     * @return
     */
    Integer deleteByParams(WhereParamType paramType);

    /**
     * 修改一条记录
     *
     * @param entity
     * @return
     */
    Integer updateByEntity(T entity);

    /**
     * 根据参数 {@link WhereParamType} 修改数据
     *
     * @param paramType
     * @return
     */
    @Update
    Integer updateByParams(WhereParamType paramType);

    /**
     * 根据主键查询对象
     *
     * @param id
     * @return
     */
    T selectById(Serializable id);

    /**
     * 根据参数查询数据
     *
     * @param paramType {@link WhereParamType}
     * @return
     */
    List<T> selectByParams(WhereParamType paramType);

    /**
     * 根据参数获取总数
     *
     * @param paramType {@link WhereParamType}
     * @return
     */
    Long selectCountByParams(WhereParamType paramType);

    /**
     * 获取总数
     *
     * @return
     */
    Long selectCount();

    /**
     * 查询所有的数据
     *
     * @return
     */
    List<T> selectEntityAll();

    /**
     * 根据参数查询数据,并分页显示
     *
     * @param paramType {@link WhereParamType}
     * @param rowBounds {@link RowBounds}
     * @return
     */
    List<T> selectPageByParams(WhereParamType paramType, RowBounds rowBounds);

    /**
     * 分页查询数据
     *
     * @param rowBounds
     * @return
     */
    List<T> selectEntityPage(RowBounds rowBounds);
}

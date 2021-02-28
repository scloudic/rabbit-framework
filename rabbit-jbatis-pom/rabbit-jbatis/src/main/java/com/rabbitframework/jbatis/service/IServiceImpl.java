package com.rabbitframework.jbatis.service;

import com.rabbitframework.jbatis.mapping.BaseMapper;
import com.rabbitframework.jbatis.mapping.RowBounds;
import com.rabbitframework.jbatis.mapping.param.WhereParamType;

import java.io.Serializable;
import java.util.List;

/**
 * 服务层接口实现类
 *
 * @param <M>
 * @param <T>
 */
public abstract class IServiceImpl<M extends BaseMapper<T>, T> implements IService<T> {

    public abstract M getBaseMapper();
    /**
     * 插入一条记录
     *
     * @param entity
     * @return
     */
    @Override
    public Integer insertByEntity(T entity) {
        return getBaseMapper().insertByEntity(entity);
    }

    /**
     * 根据主键删除一条记录
     *
     * @param id
     * @return
     */
    @Override
    public Integer deleteById(Serializable id) {
        return getBaseMapper().deleteById(id);
    }

    /**
     * 根据参数条件{@link WhereParamType }删除数据
     *
     * @param paramType {@link WhereParamType}
     * @return
     */
    @Override
    public Integer deleteByParams(WhereParamType paramType) {
        return getBaseMapper().deleteByParams(paramType);
    }

    /**
     * 修改一条记录
     *
     * @param entity
     * @return
     */
    @Override
    public Integer updateByEntity(T entity) {
        return getBaseMapper().updateByEntity(entity);
    }

    /**
     * 根据参数 {@link WhereParamType} 修改数据
     *
     * @param paramType
     * @return
     */
    @Override
    public Integer updateByParams(WhereParamType paramType) {
        return getBaseMapper().updateByParams(paramType);
    }

    /**
     * 根据主键查询对象
     *
     * @param id
     * @return
     */
    @Override
    public T selectById(Serializable id) {
        return getBaseMapper().selectById(id);
    }

    /**
     * 根据参数查询数据
     *
     * @param paramType {@link WhereParamType}
     * @return
     */
    @Override
    public List<T> selectByParams(WhereParamType paramType) {
        return getBaseMapper().selectByParams(paramType);
    }

    /**
     * 根据参数获取总数
     *
     * @param paramType {@link WhereParamType}
     * @return
     */
    @Override
    public Long selectCountByParams(WhereParamType paramType) {
        return getBaseMapper().selectCountByParams(paramType);
    }

    /**
     * 获取总数
     *
     * @return
     */
    @Override
    public Long selectCount() {
        return getBaseMapper().selectCount();
    }

    /**
     * 查询所有的数据
     *
     * @return
     */
    @Override
    public List<T> selectEntityAll() {
        return getBaseMapper().selectEntityAll();
    }

    /**
     * 根据参数查询数据,并分页显示
     *
     * @param paramType {@link WhereParamType}
     * @param rowBounds {@link RowBounds}
     * @return
     */
    @Override
    public List<T> selectPageByParams(WhereParamType paramType, RowBounds rowBounds) {
        return getBaseMapper().selectPageByParams(paramType, rowBounds);
    }

    /**
     * 分页查询数据
     *
     * @param rowBounds
     * @return
     */
    @Override
    public List<T> selectEntityPage(RowBounds rowBounds) {
        return getBaseMapper().selectEntityPage(rowBounds);
    }
}

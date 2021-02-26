package com.rabbitframework.jbatis.service;

import com.rabbitframework.jbatis.mapping.BaseMapper;
import com.rabbitframework.jbatis.mapping.RowBounds;
import com.rabbitframework.jbatis.mapping.param.WhereParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 服务层接口实现类
 *
 * @param <M>
 * @param <T>
 */
public class IServiceImpl<M extends BaseMapper<T>, T> implements IService<T> {
    @Autowired
    protected M baseMapper;

    /**
     * 插入一条记录
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer insertByEntity(T entity) {
        return baseMapper.insertByEntity(entity);
    }

    /**
     * 根据主键删除一条记录
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteById(Serializable id) {
        return baseMapper.deleteById(id);
    }

    /**
     * 根据参数条件{@link WhereParamType }删除数据
     *
     * @param paramType {@link WhereParamType}
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteByParams(WhereParamType paramType) {
        return baseMapper.deleteByParams(paramType);
    }

    /**
     * 修改一条记录
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateByEntity(T entity) {
        return baseMapper.updateByEntity(entity);
    }

    /**
     * 根据参数 {@link WhereParamType} 修改数据
     *
     * @param paramType
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateByParams(WhereParamType paramType) {
        return baseMapper.updateByParams(paramType);
    }

    /**
     * 根据主键查询对象
     *
     * @param id
     * @return
     */
    @Override
    public T selectById(Serializable id) {
        return baseMapper.selectById(id);
    }

    /**
     * 根据参数查询数据
     *
     * @param paramType {@link WhereParamType}
     * @return
     */
    @Override
    public List<T> selectByParams(WhereParamType paramType) {
        return baseMapper.selectByParams(paramType);
    }

    /**
     * 根据参数获取总数
     *
     * @param paramType {@link WhereParamType}
     * @return
     */
    @Override
    public Long selectCountByParams(WhereParamType paramType) {
        return baseMapper.selectCountByParams(paramType);
    }

    /**
     * 获取总数
     *
     * @return
     */
    @Override
    public Long selectCount() {
        return baseMapper.selectCount();
    }

    /**
     * 查询所有的数据
     *
     * @return
     */
    @Override
    public List<T> selectEntityAll() {
        return baseMapper.selectEntityAll();
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
        return baseMapper.selectPageByParams(paramType, rowBounds);
    }

    /**
     * 分页查询数据
     *
     * @param rowBounds
     * @return
     */
    @Override
    public List<T> selectEntityPage(RowBounds rowBounds) {
        return baseMapper.selectEntityPage(rowBounds);
    }
}

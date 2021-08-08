package com.scloudic.rabbitframework.jbatis.service;

import com.scloudic.rabbitframework.jbatis.mapping.BaseMapper;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.mapping.param.Where;

import java.io.Serializable;
import java.util.List;

/**
 * 服务层接口实现类
 */
public abstract class IServiceImpl<M extends BaseMapper<T>, T> implements IService<T> {

    public abstract M getBaseMapper();


    @Override
    public Integer insertByEntity(T entity) {
        return getBaseMapper().insertByEntity(entity);
    }


    @Override
    public Integer deleteById(Serializable id) {
        return getBaseMapper().deleteById(id);
    }


    @Override
    public Integer deleteByParams(Where where) {
        return getBaseMapper().deleteByParams(where);
    }


    @Override
    public Integer updateByEntity(T entity) {
        return getBaseMapper().updateByEntity(entity);
    }


    @Override
    public Integer updateByParams(Where where) {
        return getBaseMapper().updateByParams(where);
    }


    @Override
    public T selectById(Serializable id) {
        return getBaseMapper().selectById(id);
    }


    @Override
    public List<T> selectByParams(Where where) {
        return getBaseMapper().selectByParams(where);
    }


    @Override
    public Long selectCountByParams(Where where) {
        return getBaseMapper().selectCountByParams(where);
    }


    @Override
    public Long selectCount() {
        return getBaseMapper().selectCount();
    }

    @Override
    public List<T> selectEntityAll() {
        return getBaseMapper().selectEntityAll();
    }


    @Override
    public List<T> selectPageByParams(Where where, RowBounds rowBounds) {
        return getBaseMapper().selectPageByParams(where, rowBounds);
    }

    @Override
    public List<T> selectEntityPage(RowBounds rowBounds) {
        return getBaseMapper().selectEntityPage(rowBounds);
    }
    
    @Override
    public T selectOneByParams(Where where) {
        return getBaseMapper().selectOneByParams(where);
    }
}

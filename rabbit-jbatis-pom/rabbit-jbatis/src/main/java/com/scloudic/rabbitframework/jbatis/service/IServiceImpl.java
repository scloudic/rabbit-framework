package com.scloudic.rabbitframework.jbatis.service;

import com.scloudic.rabbitframework.jbatis.mapping.BaseMapper;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.mapping.param.Where;

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
     * 根据参数条件{@link Where }删除数据
     *
     * @param where {@link Where}
     * @return
     */
    @Override
    public Integer deleteByParams(Where where) {
        return getBaseMapper().deleteByParams(where);
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
     * 根据参数 {@link Where} 修改数据
     *
     * @param where
     * @return
     */
    @Override
    public Integer updateByParams(Where where) {
        return getBaseMapper().updateByParams(where);
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
     * @param where {@link Where}
     * @return
     */
    @Override
    public List<T> selectByParams(Where where) {
        return getBaseMapper().selectByParams(where);
    }

    /**
     * 根据参数获取总数
     *
     * @param where {@link Where}
     * @return
     */
    @Override
    public Long selectCountByParams(Where where) {
        return getBaseMapper().selectCountByParams(where);
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
     * @param where {@link Where}
     * @param rowBounds {@link RowBounds}
     * @return
     */
    @Override
    public List<T> selectPageByParams(Where where, RowBounds rowBounds) {
        return getBaseMapper().selectPageByParams(where, rowBounds);
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

    /**
     * 根据参数获取唯一对象
     *
     * @param where
     * @return
     */
    @Override
    public T selectOneByParams(Where where) {
        return getBaseMapper().selectOneByParams(where);
    }
}

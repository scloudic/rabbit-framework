package com.scloudic.rabbitframework.jbatis.service;

import com.scloudic.rabbitframework.jbatis.annontations.Update;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.mapping.param.Where;

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
     * 根据参数条件{@link Where }删除数据
     *
     * @param paramType {@link Where}
     * @return
     */
    Integer deleteByParams(Where paramType);

    /**
     * 修改一条记录
     *
     * @param entity
     * @return
     */
    Integer updateByEntity(T entity);

    /**
     * 根据参数 {@link Where} 修改数据
     *
     * @param where
     * @return
     */
    @Update
    Integer updateByParams(Where where);

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
     * @param where {@link Where}
     * @return
     */
    List<T> selectByParams(Where where);

    /**
     * 根据参数获取总数
     *
     * @param where {@link Where}
     * @return
     */
    Long selectCountByParams(Where where);

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
     * @param where {@link Where}
     * @param rowBounds {@link RowBounds}
     * @return
     */
    List<T> selectPageByParams(Where where, RowBounds rowBounds);

    /**
     * 分页查询数据
     *
     * @param rowBounds
     * @return
     */
    List<T> selectEntityPage(RowBounds rowBounds);

    /**
     * 根据参数获取唯一对象
     *
     * @param where
     * @return
     */
    T selectOneByParams(Where where);
}

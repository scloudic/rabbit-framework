package com.scloudic.rabbitframework.jbatis.mapping;

import com.scloudic.rabbitframework.jbatis.mapping.param.Where;

import java.io.Serializable;
import java.util.List;

/**
 * mapper基类接口
 *
 * @author: justin
 */
public interface BaseMapper<T> {
    /**
     * 插入一条记录
     *
     * @param entity entity
     * @return 返回成功与否
     */
    Integer insertByEntity(T entity);

    /**
     * 批量插入数据
     *
     * @param entity list集合
     * @return 返回批次数量
     */
    public int batchInsertEntity(List<T> entity);

    /**
     * 根据主键删除一条记录
     *
     * @param id id
     * @return int
     */
    Integer deleteById(Serializable id);

    /**
     * 根据参数条件{@link Where }删除数据
     *
     * @param where {@link Where}
     * @return int
     */
    Integer deleteByParams(Where where);

    /**
     * 修改一条记录
     *
     * @param entity 实体
     * @return int
     */
    Integer updateByEntity(T entity);

    /**
     * 根据参数 {@link Where} 修改数据
     *
     * @param where where
     * @return int
     */
    Integer updateByParams(Where where);

    /**
     * 根据主键查询对象
     *
     * @param id id
     * @return T
     */
    T selectById(Serializable id);

    /**
     * 根据参数获取唯一对象
     *
     * @param where where
     * @return T
     */
    T selectOneByParams(Where where);

    /**
     * 根据参数查询数据
     *
     * @param where {@link Where}
     * @return list
     */
    List<T> selectByParams(Where where);

    /**
     * 根据参数获取总数
     *
     * @param where {@link Where}
     * @return long
     */
    Long selectCountByParams(Where where);

    /**
     * 获取总数
     *
     * @return long
     */
    Long selectCount();

    /**
     * 查询所有的数据
     *
     * @return list
     */
    List<T> selectEntityAll();

    /**
     * 根据参数查询数据,并分页显示
     *
     * @param where     {@link Where}
     * @param rowBounds {@link RowBounds}
     * @return list
     */
    List<T> selectPageByParams(Where where, RowBounds rowBounds);

    /**
     * 分页查询数据
     *
     * @param rowBounds rowBounds
     * @return list
     */
    List<T> selectEntityPage(RowBounds rowBounds);
}

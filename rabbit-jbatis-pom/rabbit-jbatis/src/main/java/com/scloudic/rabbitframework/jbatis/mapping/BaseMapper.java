package com.scloudic.rabbitframework.jbatis.mapping;

import com.scloudic.rabbitframework.jbatis.annontations.Param;
import com.scloudic.rabbitframework.jbatis.annontations.SQL;
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
     * 动态表插入
     *
     * @param entity      实体
     * @param tableSuffix 动态表后缀
     * @return int
     */
    Integer insertDynamicTable(@Param("entity") T entity, @Param("tableSuffix") String tableSuffix);


    /**
     * 批量插入数据
     *
     * @param entity list集合
     * @return 返回批次数量
     */
    int batchInsertEntity(List<T> entity);

    /**
     * 根据主键删除一条记录
     *
     * @param id id
     * @return int
     */
    Integer deleteById(Serializable id);

    /**
     * 根据主键删除动态表记录
     *
     * @param id          主键
     * @param tableSuffix 动态表后缀
     * @return int
     */
    Integer deleteDynamicTableById(@Param("id") Serializable id, @Param("tableSuffix") String tableSuffix);

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
     * 根据实体主键修改动态表记录
     *
     * @param entity
     * @param tableSuffix
     * @return int
     */
    Integer updateDynamicTable(@Param("entity") T entity, @Param("tableSuffix") String tableSuffix);

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
     * 根据主键查询动态表记录
     *
     * @param id          主键
     * @param tableSuffix 动态表后缀
     * @return t
     */
    T selectDynamicTableById(@Param("id") Serializable id, @Param("tableSuffix") String tableSuffix);

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
     * 获取动态表总数
     *
     * @param tableSuffix 动态表后缀
     * @return long
     */
    Long selectDynamicTableCount(@Param("tableSuffix") String tableSuffix);

    /**
     * 查询所有的数据
     *
     * @return list
     */
    List<T> selectEntityAll();

    /**
     * 查询动态表所有数据
     *
     * @param tableSuffix 动态表后缀
     * @return list
     */
    List<T> selectDynamicTableEntityAll(@Param("tableSuffix") String tableSuffix);

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

    /**
     * 分页查询动态表数据
     *
     * @param rowBounds   rowBounds
     * @param tableSuffix 动态表后缀
     * @return list
     */
    List<T> selectDynamicTableEntityPage(RowBounds rowBounds, @Param("tableSuffix") String tableSuffix);

    /**
     * 动态SQL查询
     *
     * @param sql SQL语句
     * @return list
     */
    List<T> selectSQL(@SQL String sql);

    /**
     * 动态SQL分页查询
     *
     * @param sql       SQL语句
     * @param rowBounds {@link RowBounds}
     * @return list
     */
    List<T> selectPageSQL(@SQL String sql, RowBounds rowBounds);

    /**
     * 动态SQL分页条件查询
     *
     * @param sql       SQL语句
     * @param where     {@link Where}
     * @param rowBounds {@link RowBounds}
     * @return list
     */
    List<T> selectWherePageSQL(@SQL String sql, Where where, RowBounds rowBounds);

    /**
     * 动态SQL分页查询
     *
     * @param sql   SQL语句
     * @param where {@link Where}
     * @return list
     */
    List<T> selectWhereSQL(@SQL String sql, Where where);
}

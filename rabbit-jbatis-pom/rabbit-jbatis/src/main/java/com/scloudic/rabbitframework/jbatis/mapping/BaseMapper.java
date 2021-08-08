package com.scloudic.rabbitframework.jbatis.mapping;

import java.io.Serializable;
import java.util.List;

import com.scloudic.rabbitframework.jbatis.annontations.Delete;
import com.scloudic.rabbitframework.jbatis.annontations.Insert;
import com.scloudic.rabbitframework.jbatis.annontations.Select;
import com.scloudic.rabbitframework.jbatis.annontations.Update;
import com.scloudic.rabbitframework.jbatis.mapping.param.Where;

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
     * @return int
     */
    @Insert
    Integer insertByEntity(T entity);

    /**
     * 根据主键删除一条记录
     *
     * @param id id
     * @return int
     */
    @Delete("delete from @{T} where @{entityId}=#{id}")
    Integer deleteById(Serializable id);

    /**
     * 根据参数条件{@link Where }删除数据
     *
     * @param where {@link Where}
     * @return int
     */
    @Delete("delete from @{T} where 1=1 ")
    Integer deleteByParams(Where where);

    /**
     *  修改一条记录
     * @param entity 实体
     * @return int
     */
    @Update
    Integer updateByEntity(T entity);

    /**
     * 根据参数 {@link Where} 修改数据
     *
     * @param where where
     * @return int
     */
    @Update
    Integer updateByParams(Where where);

    /**
     * 根据主键查询对象
     *
     * @param id id
     * @return T
     */
    @Select("select * from @{T} where @{entityId}=#{id}")
    T selectById(Serializable id);

    /**
     * 根据参数获取唯一对象
     *
     * @param where where
     * @return T
     */
    @Select("select ${showColumns} from @{T} where 1=1 ")
    T selectOneByParams(Where where);

    /**
     * 根据参数查询数据
     *
     * @param where {@link Where}
     * @return list
     */
    @Select("select ${showColumns} from @{T} where 1=1 ")
    List<T> selectByParams(Where where);

    /**
     * 根据参数获取总数
     *
     * @param where {@link Where}
     * @return long
     */
    @Select("select count(1) from @{T} where 1=1 ")
    Long selectCountByParams(Where where);

    /**
     * 获取总数
     *
     * @return long
     */
    @Select("select count(1) from @{T} ")
    Long selectCount();

    /**
     * 查询所有的数据
     *
     * @return list
     */
    @Select("select * from @{T} ")
    List<T> selectEntityAll();

    /**
     * 根据参数查询数据,并分页显示
     *
     * @param where     {@link Where}
     * @param rowBounds {@link RowBounds}
     * @return list
     */
    @Select("select ${showColumns} from @{T} where 1=1 ")
    List<T> selectPageByParams(Where where, RowBounds rowBounds);

    /**
     * 分页查询数据
     *
     * @param rowBounds rowBounds
     * @return list
     */
    @Select("select * from @{T} where 1=1 ")
    List<T> selectEntityPage(RowBounds rowBounds);
}

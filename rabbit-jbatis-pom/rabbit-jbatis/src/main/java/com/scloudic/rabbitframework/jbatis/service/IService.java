package com.scloudic.rabbitframework.jbatis.service;

import com.scloudic.rabbitframework.core.utils.PageBean;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.mapping.param.Where;

import java.io.Serializable;
import java.util.List;

/**
 * 服务层通用接口类
 */
public interface IService<T> {

    Integer insertByEntity(T entity);

    /**
     * 动态表插入
     *
     * @param entity          实体
     * @param tableNameSuffix 动态表后缀
     * @return
     */
    Integer insertDynamicTable(T entity, String tableNameSuffix);

    /**
     * 批量插入数据
     *
     * @param entity list集合
     * @return 返回批次数量
     */
    int batchInsertEntity(List<T> entity);


    Integer deleteById(Serializable id);

    /**
     * 根据主键删除动态表记录
     *
     * @param id          主键
     * @param tableSuffix 动态表名后缀
     * @return
     */
    Integer deleteDynamicTableById(Serializable id, String tableSuffix);

    Integer deleteByParams(Where paramType);

    Integer updateByEntity(T entity);

    Integer updateByParams(Where where);

    Integer updateDynamicTable(T entity, String tableSuffix);

    T selectById(Serializable id);

    T selectDynamicTableById(Serializable id, String tableSuffix);

    List<T> selectByParams(Where where);


    Long selectCountByParams(Where where);


    Long selectCount();

    Long selectDynamicTableCount(String tableSuffix);

    List<T> selectEntityAll();

    List<T> selectDynamicTableEntityAll(String tableSuffix);


    List<T> selectPageByParams(Where where, RowBounds rowBounds);

    List<T> selectEntityPage(RowBounds rowBounds);

    List<T> selectDynamicTableEntityPage(RowBounds rowBounds, String tableSuffix);

    T selectOneByParams(Where where);

    PageBean<T> selectPageBeanByParams(Where where, Long pageNum, Long pageSize);
}

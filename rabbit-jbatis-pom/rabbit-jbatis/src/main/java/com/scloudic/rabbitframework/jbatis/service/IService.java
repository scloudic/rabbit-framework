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
     * 批量插入数据
     *
     * @param entity list集合
     * @return 返回批次数量
     */
    public int batchInsertEntity(List<T> entity);
    

    Integer deleteById(Serializable id);


    Integer deleteByParams(Where paramType);

    Integer updateByEntity(T entity);

    Integer updateByParams(Where where);


    T selectById(Serializable id);

    List<T> selectByParams(Where where);


    Long selectCountByParams(Where where);


    Long selectCount();


    List<T> selectEntityAll();


    List<T> selectPageByParams(Where where, RowBounds rowBounds);

    List<T> selectEntityPage(RowBounds rowBounds);

    T selectOneByParams(Where where);

    PageBean<T> selectPageBeanByParams(Where where, Long pageNum, Long pageSize);
}

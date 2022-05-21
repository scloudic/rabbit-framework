package com.scloudic.rabbitframework.jbatis.service;

import com.scloudic.rabbitframework.core.utils.PageBean;
import com.scloudic.rabbitframework.jbatis.mapping.BaseMapper;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.mapping.param.Where;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 动态表插入
     *
     * @param entity          实体
     * @param tableNameSuffix 动态表后缀
     * @return
     */
    @Override
    public Integer insertDynamicTable(T entity, String tableNameSuffix) {
        return getBaseMapper().insertDynamicTable(entity, tableNameSuffix);
    }

    /**
     * 批量插入数据
     *
     * @param entity list集合
     * @return 返回批次数量
     */
    @Transactional
    @Override
    public int batchInsertEntity(List<T> entity) {
        return getBaseMapper().batchInsertEntity(entity);
    }

    @Override
    public Integer deleteById(Serializable id) {
        return getBaseMapper().deleteById(id);
    }

    /**
     * 根据主键删除动态表记录
     *
     * @param id          主键
     * @param tableSuffix 动态表名后缀
     * @return
     */
    @Override
    public Integer deleteDynamicTableById(Serializable id, String tableSuffix) {
        return getBaseMapper().deleteDynamicTableById(id, tableSuffix);
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
    public Integer updateDynamicTable(T entity, String tableSuffix) {
        return getBaseMapper().updateDynamicTable(entity, tableSuffix);
    }

    @Override
    public T selectById(Serializable id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    public T selectDynamicTableById(Serializable id, String tableSuffix) {
        return getBaseMapper().selectDynamicTableById(id, tableSuffix);
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
    public Long selectDynamicTableCount(String tableSuffix) {
        return getBaseMapper().selectDynamicTableCount(tableSuffix);
    }

    @Override
    public List<T> selectEntityAll() {
        return getBaseMapper().selectEntityAll();
    }


    @Override
    public List<T> selectDynamicTableEntityAll(String tableSuffix) {
        return getBaseMapper().selectDynamicTableEntityAll(tableSuffix);
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

    @Override
    public List<T> selectDynamicTableEntityPage(RowBounds rowBounds, String tableSuffix) {
        return getBaseMapper().selectDynamicTableEntityPage(rowBounds, tableSuffix);
    }

    @Transactional(readOnly = true)
    @Override
    public PageBean<T> selectPageBeanByParams(Where where, Long pageNum, Long pageSize) {
        Long totalCount = getBaseMapper().selectCountByParams(where);
        PageBean<T> pageBean = new PageBean<T>(pageNum, pageSize, totalCount);
        List<T> list = getBaseMapper().selectPageByParams(where, new RowBounds(pageBean.getStartPage(), pageBean.getPageSize()));

        pageBean.setDatas(list);
        return pageBean;
    }
}

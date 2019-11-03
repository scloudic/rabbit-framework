package com.rabbitframework.jade.mapping;

import java.io.Serializable;
import java.util.List;

import com.rabbitframework.jade.annontations.Delete;
import com.rabbitframework.jade.annontations.Insert;
import com.rabbitframework.jade.annontations.Select;
import com.rabbitframework.jade.annontations.Update;
import com.rabbitframework.jade.mapping.param.WhereParamType;

/**
 * mapper基类接口
 *
 * @author: justin
 * @date: 2017-07-16 下午12:50
 */
public interface BaseMapper<T> {
	/**
	 * 插入一条记录
	 *
	 * @param entity
	 * @return
	 */
	@Insert
	Integer insertByEntity(T entity);

	/**
	 * 根据主键删除一条记录
	 *
	 * @param id
	 * @return
	 */
	@Delete("delete from @{T} where @{entityId}=#{id}")
	Integer deleteById(Serializable id);

	/**
	 * 根据参数条件{@link WhereParamType }删除数据
	 *
	 * @param paramType
	 *            {@link WhereParamType}
	 * @return
	 */
	@Delete("delete from @{T} where 1=1 ")
	Integer deleteByParams(WhereParamType paramType);

	/**
	 * 修改一条记录
	 *
	 * @param entity
	 * @return
	 */
	@Update
	Integer updateByEntity(T entity);

	/**
	 * 根据主键查询对象
	 *
	 * @param id
	 * @return
	 */
	@Select("select * from @{T} where @{entityId}=#{id}")
	T selectById(Serializable id);

	/**
	 * 根据参数查询数据
	 *
	 * @param paramType
	 *            {@link WhereParamType}
	 * @return
	 */
	@Select("select * from @{T} where 1=1 ")
	List<T> selectByParams(WhereParamType paramType);

	/**
	 * 根据参数获取总数
	 *
	 * @param paramType
	 *            {@link WhereParamType}
	 * @return
	 */
	@Select("select count(1) from @{T} where 1=1 ")
	long selectCountByParams(WhereParamType paramType);

	/**
	 * 获取总数
	 *
	 * @param paramType
	 *            {@link WhereParamType}
	 * @return
	 */
	@Select("select count(1) from @{T} ")
	long selectCount();

	/**
	 * 查询所有的数据
	 *
	 * @return
	 */
	@Select("select * from @{T} ")
	List<T> selectEntityAll();

	/**
	 * 根据参数查询数据,并分页显示
	 *
	 * @param paramType
	 *            {@link WhereParamType}
	 * @param rowBounds
	 *            {@link RowBounds}
	 * @return
	 */
	@Select("select * from @{T} where 1=1 ")
	List<T> selectPageByParams(WhereParamType paramType, RowBounds rowBounds);

	/**
	 * 分页查询数据
	 * 
	 * @param rowBounds
	 * @return
	 */
	@Select("select * from @{T} where 1=1 ")
	List<T> selectEntityPage(RowBounds rowBounds);
}

package com.scloudic.rabbitframework.jbatis.dataaccess.dialect;

/**
 * 数据库方言接口类，主要用于分页处理
 */
public interface Dialect {
	public static final String OFFSET = "offset";
	public static final String LIMIT = "limit";

	public String getSQL(String sqlSrc);
}

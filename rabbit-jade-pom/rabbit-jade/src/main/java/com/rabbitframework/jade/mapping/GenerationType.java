package com.rabbitframework.jade.mapping;

/**
 * 数据库主键生成类型
 * 
 * @author Justin Liang
 *
 *
 */
public enum GenerationType {
	/**
	 * 序列方式自动生成 oracle
	 */
	SEQUENCE,
	/**
	 * 数据库自动生成
	 */
	IDENTITY,
	/**
	 * 手动
	 */
	MANUAL
}
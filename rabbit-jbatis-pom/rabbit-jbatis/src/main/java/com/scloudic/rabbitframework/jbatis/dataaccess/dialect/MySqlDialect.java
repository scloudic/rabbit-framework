package com.scloudic.rabbitframework.jbatis.dataaccess.dialect;

public class MySqlDialect implements Dialect {
	private static String LIMITAFTER = " limit #{offset},#{limit} ";

	public String getSQL(String sqlSrc) {
		StringBuilder sb = new StringBuilder();
		sb.append(sqlSrc);
		sb.append(LIMITAFTER);
		return sb.toString();
	}

}

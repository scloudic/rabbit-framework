package com.rabbitframework.jbatis.dataaccess.dialect;

public class OracleDialect implements Dialect {
	private static String LIMITBEFOR = "select * from ( select a.*, ROWNUM rnum from ( ";
	private static String LIMITAFTER = "  ) a where ROWNUM <= #{limit}) where rnum  > #{offset}";

	public String getSQL(String sqlSrc) {
		StringBuilder sb = new StringBuilder();
		sb.append(LIMITBEFOR);
		sb.append(sqlSrc);
		sb.append(LIMITAFTER);
		return sb.toString();
	}
}
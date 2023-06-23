package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;
import com.scloudic.rabbitframework.jbatis.mapping.param.Where;

public class CustomerSelect extends BaseSQLParser {
    @Override
    public String parserSQL() {
        if (paramType != Where.class) {
            return sqlScript;
        }
        String sqlValue = "";
        String where = getSearchSql();
        sqlValue = sqlScript + " " + where + " ";
        StringBuilder sb = new StringBuilder();
        sb.append("<if test=\"defCondition\" > ${defineCondition} </if>");
        sb.append("<if test=\"groupBy != null\" > group by ${groupBy} </if>");
        sb.append("<if test=\"having != null\" > having ${having} </if>");
        sb.append("<if test=\"orderBy != null\" > order by ${orderBy} </if>");
        sqlValue = sqlValue + sb.toString();
        return sqlValue;
    }


    @Override
    public SqlCommendType getSqlCommendType() {
        return SqlCommendType.SELECT;
    }
}

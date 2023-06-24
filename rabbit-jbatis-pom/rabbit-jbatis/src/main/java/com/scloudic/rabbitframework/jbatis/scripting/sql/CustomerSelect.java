package com.scloudic.rabbitframework.jbatis.scripting.sql;

import com.scloudic.rabbitframework.core.propertytoken.PropertyParser;
import com.scloudic.rabbitframework.jbatis.mapping.SqlCommendType;
import com.scloudic.rabbitframework.jbatis.mapping.param.Where;

import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerSelect extends BaseSQLParser {
    @Override
    public String parserSQL() {
        if (paramType != Where.class) {
            return sqlScript;
        }
        String sqlValue = "";
        String where = getSearchSql();
        Pattern pattern = Pattern.compile("\\$\\$\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(sqlScript);
        ArrayList<String> values = new ArrayList<String>();
        while (matcher.find()) {
            values.add(matcher.group(1));
        }
        if (values.size() > 0) {
            Properties properties = new Properties();
            properties.put(values.get(0), where);
            sqlValue = PropertyParser.parseOther("$${", "}", sqlScript, properties);
        } else {
            sqlValue = sqlScript + " " + where + " ";
        }
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

package com.rabbitframework.dbase.builder;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rabbitframework.dbase.annontations.Insert;
import com.rabbitframework.dbase.mapping.EntityMap;
import com.rabbitframework.dbase.mapping.EntityProperty;
import com.rabbitframework.dbase.mapping.GenerationType;
import com.rabbitframework.dbase.mapping.SqlCommendType;
import com.rabbitframework.dbase.mapping.binding.EntityRegistry;
import com.rabbitframework.dbase.mapping.param.WhereParamType;
import com.tjzq.commons.propertytoken.PropertyParser;
import com.tjzq.commons.utils.ReflectUtils;
import com.tjzq.commons.utils.StringUtils;

/**
 * sql脚本解析生成
 *
 * @author: justin
 * @date: 2017-07-19 23:48
 */
public class SQLParser {

	private String sqlValue;
	private SqlCommendType sqlCommendType;
	private Class<?> paramType;
	private boolean batchUpdate = false;

	public SQLParser(Method method, String sqlValueSrc, SqlCommendType sqlCommendType, Class<?> paramType,
			Configuration configuration) {
		this.sqlCommendType = sqlCommendType;
		this.paramType = paramType;
		if (sqlCommendType == SqlCommendType.INSERT || sqlCommendType == SqlCommendType.UPDATE) {
			if (sqlCommendType == SqlCommendType.INSERT) {
				Insert insert = method.getAnnotation(Insert.class);
				batchUpdate = insert.batch();
				setParamType(method);
			}
			getInsertOrUpdateSql(sqlValueSrc, configuration);
		} else if (sqlCommendType == SqlCommendType.SELECT && paramType == WhereParamType.class) {
			String where = getSearchSql();
			Pattern pattern = Pattern.compile("\\$\\$\\{(.*?)\\}");
			Matcher matcher = pattern.matcher(sqlValueSrc);
			ArrayList<String> values = new ArrayList<String>();
			while (matcher.find()) {
				values.add(matcher.group(1));
			}
			if (values.size() > 0) {
				Properties properties = new Properties();
				properties.put(values.get(0), where);
				this.sqlValue = PropertyParser.parseOther("$${", "}", sqlValueSrc, properties);
			} else {
				this.sqlValue = sqlValueSrc + " " + where + " ";
			}
			String orderBy = "<if test=\"orderby != null\" > order by ${orderby} </if>";
			String defineCondition = "<if test=\"defCondition\" > ${defineCondition} </if>";
			this.sqlValue = this.sqlValue + defineCondition + orderBy;
		} else {
			this.sqlValue = sqlValueSrc;
		}
		/**
		 * 在修改或删除时增加{@link WhereParamType} 做为参数传递
		 **/
		if (paramType == WhereParamType.class
				&& (sqlCommendType == SqlCommendType.DELETE || sqlCommendType == SqlCommendType.UPDATE)) {
			this.sqlValue = this.sqlValue + " " + getSearchSql() + " ";
		}
	}

	public SqlCommendType getSqlCommendType() {
		return sqlCommendType;
	}

	public boolean isBatchUpdate() {
		return batchUpdate;
	}

	public String getSqlValue() {
		return sqlValue;
	}

	private String getSearchSql() {
		return "<where>" + "<foreach collection=\"oredCriteria\" item=\"criteria\" separator=\"or\" >"
				+ "<if test=\"criteria.valid\" >" + "<trim prefix=\"(\" suffix=\")\" prefixOverrides=\"and\" >"
				+ "<foreach collection=\"criteria.criteria\" item=\"criterion\" >" + "<choose>"
				+ "<when test=\"criterion.noValue\" >" + " and ${criterion.condition}" + "</when>"
				+ "<when test=\"criterion.singleValue\" >" + " and ${criterion.condition} #{criterion.value}"
				+ "</when>" + "<when test=\"criterion.betweenValue\" >"
				+ " and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}" + "</when>"
				+ "<when test=\"criterion.listValue\" >" + " and ${criterion.condition}"
				+ "<foreach collection=\"criterion.value\" item=\"listItem\" open=\"(\" close=\")\" separator=\",\" >"
				+ "#{listItem}" + "</foreach>" + "</when>" + "</choose>" + "</foreach>" + "</trim>" + "</if>"
				+ "</foreach>" + "</where>";
	}

	private void getInsertOrUpdateSql(String sqlValueSrc, Configuration configuration) {
		this.sqlValue = sqlValueSrc;
		if (StringUtils.isBlank(sqlValueSrc) && this.paramType != null) {
			EntityRegistry entityRegistry = configuration.getEntityRegistry();
			String paramTypeName = this.paramType.getName();
			boolean isEntity = entityRegistry.hasEntityMap(paramTypeName);
			if (isEntity) {
				EntityMap entityMap = entityRegistry.getEntityMap(paramTypeName);
				if (sqlCommendType == SqlCommendType.INSERT) {
					if (isBatchUpdate()) {
						this.sqlValue = getUpdateInsertSql(entityMap);
					} else {
						this.sqlValue = getInsertSql(entityMap);
					}
				} else {
					this.sqlValue = getUpdateSql(entityMap);
				}
			}
		}
	}

	private void setParamType(Method method) {
		if (batchUpdate) {
			Type[] parameters = method.getGenericParameterTypes();
			Type parameter = parameters[0];
			Type[] t = ((ParameterizedType) parameter).getActualTypeArguments();
			paramType = ReflectUtils.getGenericClassByType(t[0]);
		}
	}

	public Class<?> getParamType() {
		return paramType;
	}

	private String getUpdateSql(EntityMap entityMap) {
		StringBuilder sbPrefix = new StringBuilder();
		List<EntityProperty> propertyMapping = entityMap.getColumnProperties();
		sbPrefix.append("update ");
		sbPrefix.append(entityMap.getTableName());
		sbPrefix.append(" ");
		sbPrefix.append("<trim prefix=\"set \" suffixOverrides=\",\" >");

		for (EntityProperty entityMapping : propertyMapping) {
			String column = entityMapping.getColumn();
			String property = entityMapping.getProperty();
			sbPrefix.append("<if test=\"" + property + " != null\" >").append(column).append("=").append("#{")
					.append(property).append("}").append(",").append("</if>");
		}
		sbPrefix.append("</trim>");
		sbPrefix.append(" ");
		sbPrefix.append(" where ");
		sbPrefix.append("<trim suffix=\" \" suffixOverrides=\"and\" >");
		List<EntityProperty> idMapping = entityMap.getIdProperties();
		for (EntityProperty entityMapping : idMapping) {
			String column = entityMapping.getColumn();
			String property = entityMapping.getProperty();
			sbPrefix.append(column).append("=").append("#{").append(property).append("}").append(" and ");
		}
		sbPrefix.append("</trim>");
		String updateSqlScript = sbPrefix.toString();
		return updateSqlScript;
	}

	private String getInsertSql(EntityMap entityMap) {
		StringBuilder sbPrefix = new StringBuilder();
		sbPrefix.append(" insert into ");
		sbPrefix.append(entityMap.getTableName());
		sbPrefix.append(" ");
		sbPrefix.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
		StringBuilder sbSuffix = new StringBuilder();
		sbSuffix.append("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
		List<EntityProperty> identityMapping = entityMap.getIdProperties();
		for (EntityProperty entityMapping : identityMapping) {
			String column = entityMapping.getColumn();
			String property = entityMapping.getProperty();
			GenerationType genType = entityMapping.getGenerationType();
			if (GenerationType.IDENTITY.equals(genType)) {
				continue;
			}
			sbPrefix.append(column);
			sbPrefix.append(",");
			if (GenerationType.SEQUENCE.equals(genType)) {
				sbSuffix.append(entityMapping.getSelectKey());
			} else {
				sbSuffix.append("#{");
				sbSuffix.append(property);
				sbSuffix.append("}");
			}
			sbSuffix.append(",");
		}
		List<EntityProperty> propertyMapping = entityMap.getColumnProperties();
		for (EntityProperty entityMapping : propertyMapping) {
			String column = entityMapping.getColumn();
			String property = entityMapping.getProperty();
			sbPrefix.append("<if test=\"" + property + " != null\" >");
			sbPrefix.append(column);
			sbPrefix.append(",");
			sbPrefix.append("</if>");
			sbSuffix.append("<if test=\"" + property + " != null\" >");
			sbSuffix.append("#{");
			sbSuffix.append(property);
			sbSuffix.append("}");
			sbSuffix.append(",");
			sbSuffix.append("</if>");
		}
		sbPrefix.append("</trim>");
		sbSuffix.append("</trim>");
		String insertSql = sbPrefix.toString() + sbSuffix.toString();
		return insertSql;
	}

	private String getUpdateInsertSql(EntityMap entityMap) {
		StringBuilder sbPrefix = new StringBuilder();
		sbPrefix.append(" insert into ");
		sbPrefix.append(entityMap.getTableName());
		sbPrefix.append(" ");
		sbPrefix.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
		StringBuilder sbSuffix = new StringBuilder();
		sbSuffix.append("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
		List<EntityProperty> identityMapping = entityMap.getIdProperties();
		for (EntityProperty entityMapping : identityMapping) {
			String column = entityMapping.getColumn();
			String property = entityMapping.getProperty();
			GenerationType genType = entityMapping.getGenerationType();
			if (GenerationType.IDENTITY.equals(genType)) {
				continue;
			}
			sbPrefix.append(column);
			sbPrefix.append(",");
			if (GenerationType.SEQUENCE.equals(genType)) {
				sbSuffix.append(entityMapping.getSelectKey());
			} else {
				sbSuffix.append("#{");
				sbSuffix.append(property);
				sbSuffix.append("}");
			}
			sbSuffix.append(",");
		}
		List<EntityProperty> propertyMapping = entityMap.getColumnProperties();
		for (EntityProperty entityMapping : propertyMapping) {
			String column = entityMapping.getColumn();
			String property = entityMapping.getProperty();
			sbPrefix.append(column);
			sbPrefix.append(",");
			sbSuffix.append("#{");
			sbSuffix.append(property);
			sbSuffix.append("}");
			sbSuffix.append(",");
		}
		sbPrefix.append("</trim>");
		sbSuffix.append("</trim>");
		String insertSql = sbPrefix.toString() + sbSuffix.toString();
		return insertSql;
	}
}

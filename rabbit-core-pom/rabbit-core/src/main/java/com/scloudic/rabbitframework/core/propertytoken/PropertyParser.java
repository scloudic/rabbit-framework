package com.scloudic.rabbitframework.core.propertytoken;
import java.util.Properties;

/**
 * 
 * 属性解析器
 * 
 * @author leungjy
 * 
 */
public class PropertyParser {
	/**
	 * 
	 * 解析${}表达式，并根据属性获取值
	 * 
	 * @param expSource
	 *            参数值
	 * @param variables
	 *            属性变量
	 * @return
	 */
	public static String parseDollar(String expSource, Properties variables) {
		return parseOther("${", "}", expSource, variables);
	}

	/**
	 * 
	 * 根据表达式参数解析获取属性值
	 * 
	 * @param startExp
	 *            起始表达式
	 * @param endExp
	 *            结束表达式
	 * @param expSource
	 *            表达式源数据
	 * @param variables
	 *            属性变量
	 * @return
	 */
	public static String parseOther(String startExp, String endExp,
			String expSource, Properties variables) {
		VariableTokenHandler handler = new VariableTokenHandler(variables,
				startExp, endExp);
		GenericTokenParser parser = new GenericTokenParser(startExp, endExp,
				handler);
		return parser.parse(expSource);
	}

	private final static class VariableTokenHandler implements TokenHandler {
		private Properties variables;
		private String startExp = "";
		private String endExp = "";

		public VariableTokenHandler(Properties variables, String startExp,
				String endExp) {
			this.variables = variables;
			this.startExp = startExp;
			this.endExp = endExp;
		}

		public String handleToken(String content) {
			if (variables != null && variables.containsKey(content)) {
				return variables.getProperty(content);
			}
			return startExp + content + endExp;
		}
	}
}

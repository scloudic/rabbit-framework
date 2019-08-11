package com.rabbitframework.commons.bean;

import java.util.Properties;

import com.rabbitframework.commons.propertytoken.PropertyParser;

public class PropertyParserTest {
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put("test", "hello");
		properties.put("test1", "hello1");
		System.out.println(PropertyParser.parseDollar("${test},${test1}", properties));
		
		
	}
}

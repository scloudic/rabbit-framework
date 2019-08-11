
代码生成器配置使用示例：

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
	<properties resource="jdbc.properties">
   		<property name="test" value="generator"></property>
    </properties>
	<jdbcConnection driverClass="${jdbc.driverClassName}" catalog="" connectionURL="${jdbc.url}" userName="${jdbc.username}" password="${jdbc.password}">
	    	<!--<property name="other" value="demo"/>-->
	</jdbcConnection>
    <generators>
        <generator templatePath="template/model.ftl" targetPackage="com.test" targetProject="genpath" fileSuffix="" extension=".java"/>
    </generators>
    <!--type分为：all:所有表，assign:指定表名,
    逻辑说明：
    1、如果类型为all，将不再解析以下的table元素
    2、如果类型为assign,先解析table元素,如果没有将类型更改为all
    3、默认类型为all-->
	<tables type="all">
    	<table tableName="rabbit_role" objectName="RabbitRole"/>
	</tables>
</configuration>
```
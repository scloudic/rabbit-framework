dbase说明：

一、dbase配置,可通过定义的xml文件进行配置和spring加载项配置，以下分别使用示例说明：

1､xml配置示例：

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
	<properties resource="c3p0.properties">
		<!-- <property name="" value=""></property> -->
	</properties>
	<!-- <plugins> <plugin interceptor=""></plugin> </plugins> -->
	<!-- <caches>
		<cache name="defaultCache" class="com.scloudic.rabbitframework.dbase.cache.impl.EhcacheCache">
		</cache>
	</caches> -->
	<dataAccess>
		<dataSourceFactory class="com.scloudic.rabbitframework.dbase.dataaccess.datasource.SimpleDataSourceFactory" />
		<dataSources>
			<dataSource name="simple" dialect="mysql"
				class="com.mchange.v2.c3p0.ComboPooledDataSource">
				<property name="driverClass" value="${c3p0.driverClassName}" />
				<property name="jdbcUrl" value="${c3p0.url}" />
				<property name="user" value="${c3p0.username}" />
				<property name="password" value="${c3p0.password}" />
				<property name="checkoutTimeout" value="${c3p0.checkoutTimeout}" />
				<property name="idleConnectionTestPeriod" value="${c3p0.idleConnectionTestPeriod}" />
				<property name="initialPoolSize" value="${c3p0.initialPoolSize}" />
				<property name="maxIdleTime" value="${c3p0.maxIdleTime}" />
				<property name="maxPoolSize" value="${c3p0.maxPoolSize}" />
				<property name="minPoolSize" value="${c3p0.minPoolSize}" />
				<property name="maxStatements" value="${c3p0.maxStatements}" />
				<property name="acquireIncrement" value="${c3p0.acquireIncrement}" />
			</dataSource>
		</dataSources>
	</dataAccess>
	<entitys>
		<entity class="TestUser"></entity>
		<!-- <package name=""></package> -->
	</entitys>
	<mappers>
		<mapper class="TestUserMapper"></mapper>
		<!-- <package name=""></package> -->
	</mappers>
</configuration>
```	
2、spring配置示例：

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx" xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="
			http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.2.xsd
			http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.2.xsd
			http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.2.xsd
			http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.2.xsd">
	<!-- <context:component-scan base-package="com.rabbitfragmework.**.service.impl,com.rabbitfragmework.**.mapper" 
		/> -->
	<context:component-scan base-package="com.rabbitfragmework.**.service.impl" />
	<context:property-placeholder location="classpath*:c3p0.properties" />
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource"
		destroy-method="close">
		<property name="driverClass" value="${c3p0.driverClassName}" />
		<property name="jdbcUrl" value="${c3p0.url}" />
		<property name="user" value="${c3p0.username}" />
		<property name="password" value="${c3p0.password}" />
		<property name="checkoutTimeout" value="${c3p0.checkoutTimeout}" />
		<property name="idleConnectionTestPeriod" value="${c3p0.idleConnectionTestPeriod}" />
		<property name="initialPoolSize" value="${c3p0.initialPoolSize}" />
		<property name="maxIdleTime" value="${c3p0.maxIdleTime}" />
		<property name="maxPoolSize" value="${c3p0.maxPoolSize}" />
		<property name="minPoolSize" value="${c3p0.minPoolSize}" />
		<property name="maxStatements" value="${c3p0.maxStatements}" />
		<property name="acquireIncrement" value="${c3p0.acquireIncrement}" />
	</bean>

<bean id="dataSourcebean" class="com.scloudic.rabbitframework.dbase.dataaccess.DataSourceBean">
	<property name="dialect" value="mysql" />
	<property name="dataSource" ref="dataSource" />
</bean>
<bean id="dataSourceFactory"
	class="com.scloudic.rabbitframework.dbase.dataaccess.datasource.SimpleDataSourceFactory" />
<bean id="rabbitDbaseFactory" class="com.scloudic.rabbitframework.jbatis.spring.RabbitDbaseFactoryBean">
	<!-- <property name="configLocation" value="classpath:jbatis-config.xml"></property> -->
	<property name="entityPackages" value="com.rabbitfragmework.**.test.model"></property>
	<property name="mapperPackages" value="com.rabbitfragmework.**.test.mapper"></property>
	<property name="dataSourceFactory" ref="dataSourceFactory"></property>
	<property name="dataSourceMap">
		<map>
			<entry key="default" value-ref="dataSourcebean"></entry>
		</map>
	</property>
</bean>

<bean class="com.scloudic.rabbitframework.dbase.spring.MapperScannerConfigurer">
	<!-- <property name="annotationClass" value="org.springframework.stereotype.Repository" 
		/> -->
	<property name="basePackage" value="com.rabbitfragmework.**.test.mapper" />
	<property name="rabbitDbaseFactoryBeanName" value="rabbitDbaseFactory" />
</bean>

<!-- <bean name="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager"> 
	<property name="dataSource" ref="dataSource"></property> </bean> <tx:annotation-driven 
	transaction-manager="transactionManager" proxy-target-class="true" /> -->
<aop:aspectj-autoproxy expose-proxy="true" />
</beans>
```	
二、dbase数据源(DataSource)支持策略，数据源支持读写分离、多数据源、随机数据源、简单数据源以及自定义数据源，介绍如下：

1、读写分离策略：使用MasterSlaveDataSourceFactory类进行配置，所有写操作使用master,读操作使用slave 允许有多master/salve,在多master/salve数据源情况下，主要通过key来区分,key中只要包含有master/salve字符,程序将自动匹配,当不匹配master/savle时，将默认同时都可以使用二者.

2、多数据源策略：使用MultiDataSourceFactory类进行配置，根据Mapper注解中的catalog中的名称来确定，如果名称为空将使用默认的DataSource数据源,但是需要配置默认数据源（key必须为"default"）时，将使用默认数据源。

3､随机数据源策略：使用RandomDataSourceFactory类进行配置。

4､简单数据源策略：使用SimpleDataSourceFactory类进行配置，只添加一个数据源。


	



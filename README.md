### rabbit-framework
rabbit-framework框架是为解决对项目环境快速搭建、统一基础框架而形成的一套整体的技术框架方案，目前框架主要划分为以下模块：

一、rabbit-jbatis:数据库ORM模块,主要实现以下功能：

	1、全sql的注释方式；
	
	2、支持缓存功能；
	
	3、支持拦截器功能；
	
	4、支持多数据源功能，读写自动识别，多数据源事务可集成jta事务控制；
	
	5、支持创建表、分表分库;

二、rabbit-security:权限框架,对[shiro_1.3.2](https://github.com/apache/shiro/)进行扩展,主要扩展项如下：

    1、支持redis缓存模块。
    2、解决在使用第三方缓存时,不断刷缓存session的问题。
    3、增加权限缓存过期处理。
    4、支持token机制。
    

三、rabbit-web:web-rest框架，集成[jersey2](https://github.com/jersey/jersey)框架,封装相关接口便于快速开发

四、rabbit-generator:代码生成器模块,代码生成器通过使用配置和freemarker模板来完成,核心代码将数据库中的表结构转换为实体对象。根据配置信息将实体对象传入模板中,最终生成代码文件。目前默认模板在template/目录中，模板也可以自定义,其模板格式可以查看示例。

五、rabbit-commons模块，公共通用相关类

六、集成spring-boot,支持微服务架构
### rabbitframework整合框架
rabbitframework框架利用第三方开源框架以及对maven的依赖而整合的基础框架，能够快速搭建项目环境，封装项目中常用到的框架(如:权限框架、数据库框架)及公共类的封装等，后续将在实践中进行积累包装提取慢慢形成一套方便于项目开发的基础框架。目前框架主要划分为以下模块：

一、rabbitframework-commons:公共模块，包括对xml解释，反射机制通用类。

二、rabbitframework-dbase:数据库ORM模块,主要实现以下功能：

	1、全sql的注释方式；
	
	2、支持缓存功能；
	
	3、支持拦截器功能；
	
	4、支持多数据源功能，读写自动识别，多数据源事务可集成jta事务控制；
	
	5、支持创建表、分表分库(通过拦截器功能实现)；

三、rabbitframework-security:权限框架,对[shiro1.3.0](https://github.com/apache/shiro/)进行扩展,主要扩展项如下：

    1、支持redis缓存模块。
    2、解决在使用第三方缓存时,不断刷缓存session的问题。
    3、增加权限缓存过期处理。
    

四、rabbitframework-web:web-rest框架，集成[jersey2](https://github.com/jersey/jersey)框架，

五、rabbitframework-generator:代码生成器模块,代码生成器通过使用配置和freemarker模板来完成,核心代码将数据库中的表结构转换为实体对象。根据配置信息将实体对象传入模板中,最终生成代码文件。目前默认模板在template/目录中，模板也可以自定义,其模板格式可以查看示例。

六、示例:[learningExample](https://github.com/xuegongzi/learningExample)
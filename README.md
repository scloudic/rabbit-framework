### rabbit-framework 介绍
我们大部分软件开发都是基于ssm整合框架搭建项目，而大部分非业务代码基本上都从各种项目中迁移、复制、各jar版本也经常出现冲突等情况，
一直想着做一款能快速搭建环境、项目中只关注业务部分，避免上述等重复性工作。于是利用业余空闲时间写了一套整合型框架(rabbit-framework)。
rabbit-framework框架通过使用开源框架springBoot、redisson、shiro等技术的基础上进行封装而形成的一套项目技术框架，主要划分为以下模块：

一、rabbit-jbatis:数据库框架,基于mybatis二次开发,主要实现以下功能：

	1、全sql的注释方式,支持mybatis标签语法；
	
	2、支持缓存功能；
	
	3、支持拦截器功能；
	
	4、支持多数据源功能，读写自动识别，多数据源事务可集成jta事务控制；
	
	5、支持创建表、分表分库;
	
	6、继承RoutingDataSource类,支持多租户模式 
	

二、rabbit-security:权限框架,对[shiro_1._9_.1](https://github.com/apache/shiro/)进行扩展封装,主要扩展项如下：

    1、支持redis缓存模块。
    2、新增通过url配置权限过滤器
    3、增加权限缓存过期处理。
    4、token机制。
   
   新增权限说明
    
   | 配置名称 | 注解名称 | 权限说明|
   | :-----:| :----: |:----: |
   | uriPerms | UriPermissions | uri权限配置 |
   | rolesOr | 无 | 多角色"或" 关系配置,springboot配置如：rolesOr.login,url |

三、rabbit-web:web-rest框架,依赖于springmvc,封装相关接口便于快速开发。支持freemarker配置

四、rabbit-generator:代码生成器模块,代码生成器通过使用配置和freemarker模板来完成,核心代码将数据库中的表结构转换为实体对象。根据配置信息将实体对象传入模板中,最终生成代码文件。目前默认模板在template/目录中，模板也可以自定义,其模板格式可以查看示例。

五、rabbit-core模块:core模块是框架的核心通用模块,项目中常用的公用处理进行封装,主要包括以下：

    1、utils通用共公处理,如：string、json、UUID、date处理。
    2、XML解析方封装
    3、OkHttp的封装
    4、监听通知服务
    5、集成easyExcel
   

六、rabbit-cache:集成redis缓存

七、Java包依赖
````
   <dependency>
       <groupId>com.scloudic</groupId>
       <artifactId>rabbit-core</artifactId>
       <version>3.7.5</version>
   </dependency>
   <dependency>
       <groupId>com.scloudic</groupId>
       <artifactId>rabbit-security</artifactId>
       <version>3.7.5</version>
   </dependency>
   <dependency>
       <groupId>com.scloudic</groupId>
       <artifactId>rabbit-jbatis</artifactId>
       <version>3.7.5</version>
   </dependency>
   <dependency>
       <groupId>com.scloudic</groupId>
       <artifactId>rabbit-redisson</artifactId>
       <version>3.7.5</version>
   </dependency>
   <dependency>
       <groupId>com.scloudic</groupId>
       <artifactId>rabbit-web</artifactId>
       <version>3.7.5</version>
   </dependency>
   <dependency>
       <groupId>com.scloudic</groupId>
       <artifactId>rabbit-generator</artifactId>
       <version>3.7.5</version>
       <scope>provided</scope>
   </dependency>
   <dependency>
       <groupId>com.scloudic</groupId>
       <artifactId>rabbit-web-spring-boot-starter</artifactId>
       <version>3.7.5</version>
   </dependency>
   <dependency>
       <groupId>com.scloudic</groupId>
       <artifactId>rabbit-security-redisson-cache</artifactId>
       <version>3.7.5</version>
   </dependency>
   <dependency>
       <groupId>com.scloudic</groupId>
       <artifactId>rabbit-redisson-spring-boot-starter</artifactId>
       <version>3.7.5</version>
   </dependency>
   <dependency>
       <groupId>com.scloudic</groupId>
       <artifactId>rabbit-core-spring-boot-starter</artifactId>
       <version>3.7.5</version>
   </dependency>
   <dependency>
       <groupId>com.scloudic</groupId>
       <artifactId>rabbit-jbatis-spring-boot-starter</artifactId>
       <version>3.7.5</version>
   </dependency>
   <dependency>
       <groupId>com.scloudic</groupId>
       <artifactId>rabbit-security-spring-boot-starter</artifactId>
       <version>3.7.5</version>
   </dependency>
````

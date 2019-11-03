##### rabbitframework3.2版本说明:
	1、版本优化升级，所有版本升级到3.2
	2、升级rabbit-web中jersey2版本1.29.1
	3、去掉rabbit-security-rediscache模块
	4、新增rabbitweb-spring-boot-starter模块,支持spring boot
	5、security模块去掉cas插件,后续集成buji-pac4j
	6、security库，新增rabbit-security-spring-boot-starter库，支持springboot,同时默认session保存到redis当中。
	8、web库，新增rabbit-web-spring-boot-starter库，支持springboot
	9、该版本不兼容3.1版本，部分接口更改.
	10、重新构建，去除dbase模块，改为jade模块
	
##### rabbitframework3.1版本说明:
	1、版本优化升级，所有版本升级到3.1
	2、security框架代码优化,修改authc表单验证逻辑,验证失时，直接json方式返回。

	
##### rabbitframework3.0版本说明:
	1、版本优化升级。
	2、security新增redissoncache缓存工程
      
##### rabbitframework2.0版本说明：
    1、修改所有pom版本,升级rabbitframework版本号为2.0,抽取父pom(完成)
    2､spring升级到4.3.9.RELEASE
    3､web框架：
      A、web框架中jeasey版本升级到2.25.1
      B、集成hibernate validator表单提交数据验证，使用spring拦截器来完成。
      C、统一异常类处理。
      D、新增XSS过滤器。
      E、新增输入日志拦截，并通过log4j进行打印，以便于调试使用。
    4､dbase框架：
        A、新增Mapper基类，默认实现增、删、改、查操作;
        B、新增批量插入功能;
        C、部分优化必进;
    5、security框架：
       A、优化改进;
	
##### rabbitframework1.1.3版本说明:
	1、修改所有pom版本,升级rabbitframework版本号为1.1.3
	2、security框架：
        A、修改AbstractSecurityFilter类中filterUrl请求，匹配过滤条件将直接
        调用底层Filter的过滤方法,否则调用security的Filter。
        B、修改RabbitContextResource类,添加公共方法。
        C、新增freemarker模板扩展 类,通过web.xml中的配置对模板后缀名进行定义,
        默认为.ftl。


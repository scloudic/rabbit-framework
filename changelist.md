3.7.0 版本说明：

    1、druid、mysql版本升级到1.2.14、8.0.31
    2、代码生成支持lombok方式

3.6.9 版本说明：

    1、代码优化
    2、fastJson顺序处理
3.6.8 版本说明： 

    1、代码优化
    2、jar包升级

3.6.7 版本说明：

    1、优化jbatis
    2、jar包升级,spring-boot升级到2.7.3

3.6.6 版本说明：

    1、security优化缓存,添加SessionCacheManager管理
    2、通知类改造

3.6.5 版本说明：

    1、代码调整优化jar包升级,shiro包升级到1.9.1
    2、添加aes公共类
    3、redis锁增加注解参数
    4、修改shiro生成sessionId方式

3.6.3 版本说明：

    1、代码调整优化
    2、jar包升级

3.6.2 版本说明：

    1、代码调整优化

3.6.1 版本说明：

    1、代码调整优化
    2、优化xssFilter

3.5.2.RELEASE版本说明:

    1、代码调整优化
    2、修改httpClient

3.5.1.RELEASE版本说明:

    1、代码调整优化
    2、添加日期判断
    3、jbatis框架,where增加条件参数

3.5.0.RELEASE版本说明:

    1、jbatis框架,BaseMapper添加支持动态表名传参的新方法。
    2、jbatis框架,Where类中添加支持动态表名属性字段。
    3、优化修复问题

3.4.9.RELEASE版本说明:

    1、jbatis优化,去掉自定义datasource配置改用springboot的datasource配置。
    2、升级添加jar包。
    3、升级此版本,需将jbatis的datasource配置改成springboot的datasource配置

3.4.8.RELEASE版本说明:

    1、security中的springboot配置添加自定义过滤(Filter)
    2、redisson-springboot添加Bean的启用配置

3.4.7.RELEASE版本说明:

    1、修改security，支持获取容器session
    2、web层添加动态属性
    3、优化修复问题

3.4.6.RELEASE版本说明:

    1、修改web层，Result添加自定义状态参数。
    2、修改web层json返回解释
    3、shiro版本升级到1.8.0
    4、修复部分BUG

3.4.5.RELEASE版本说明:

    1、优化web层，添加Result统一返回类。
    2、修改json解释，使用FastJson替换默认Jackson
    3、优化日志打印信息
    4、优化验证非空判断
    5、修复部分BUG

3.4.4.RELEASE版本说明：

    1、jbatis优化,去除SQLProvider注解
    2、jbatis增加对单表分页查询返回PageBan对象
    3、jbatis中Where增加group by 及having
    4、修复部分BUG

3.4.3.RELEASE版本说明：

    1、修改缓存锁方式及修复bug
    2、升级jar包

3.4.2.RELEASE版本说明：

    1、增加jbatis事务功能，支持atomikos配置,默认使用spring的DataSourceTransactionManager事务.
    2、修改security容器，默认容器为servlet,默认缓存内存

3.4.1.RELEASE版本说明：

    1、修复jbatis中dialect获取问题

3.4.0.RELEASE版本说明：

    1、优化升级jbatis,支持AbstractRoutingDataSource(多数据源)加载,不同库的多租户机制
    2、移除jbatis事务初始化配置,由应用做事务配置
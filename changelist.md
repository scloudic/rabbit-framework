3.4.7.RELEASE版本说明:

    1、修改shiro，支持获取容器session
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
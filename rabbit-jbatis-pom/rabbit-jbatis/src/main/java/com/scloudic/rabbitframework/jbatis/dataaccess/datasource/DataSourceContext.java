package com.scloudic.rabbitframework.jbatis.dataaccess.datasource;

public class DataSourceContext {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    /**
     * 设置数据源
     *
     * @param dataSourceType 数据源型类
     */
    public static void setDataSource(String dataSourceType) {
        threadLocal.set(dataSourceType);
    }

    /**
     * 获取数据类型
     *
     * @return string
     */
    public static String getDataSource() {
        return threadLocal.get();
    }

    /**
     * 清除数据原类型
     */
    public static void cleanDataSource() {
        threadLocal.remove();
    }
}

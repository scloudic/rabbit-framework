package com.scloudic.rabbitframework.jbatis.dataaccess.datasource;

import com.scloudic.rabbitframework.jbatis.dataaccess.DataSourceBean;
import com.scloudic.rabbitframework.jbatis.dataaccess.dialect.DefaultDialect;
import com.scloudic.rabbitframework.jbatis.dataaccess.dialect.Dialect;
import com.scloudic.rabbitframework.jbatis.mapping.MappedStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

public class RandomDataSourceFactory implements DataSourceFactory {
    private Random random = new Random();
    private List<DataSourceBean> dataSources = Collections.emptyList();

    @Override
    public void addDataSource(String key, DataSourceBean dataSourceBean) {
        if (dataSourceBean == null) {
            throw new NullPointerException("dataSourceBean is null");
        }
        if (this.dataSources.size() == 0) {
            this.dataSources = new ArrayList<DataSourceBean>(dataSources);
        }
        this.dataSources.add(dataSourceBean);
    }

    @Override
    public DataSource getDataSource(MappedStatement mappedStatement) {
        if (dataSources.size() == 0) {
            throw new NullPointerException("dataSources is null");
        }
        int index = random.nextInt(dataSources.size()); // 0.. size
        return dataSources.get(index).getDataSource();
    }

    @Override
    public Dialect getDialect(MappedStatement mappedStatement) {
        if (dataSources.size() == 0) {
            throw new NullPointerException("dataSources is null");
        }
        int index = random.nextInt(dataSources.size()); // 0.. size
        String dialectStr = dataSources.get(index).getDialect();
        return DefaultDialect.newInstances(dialectStr);
    }
}

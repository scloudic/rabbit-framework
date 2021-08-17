package com.scloudic.rabbitframework.jbatis.dataaccess.datasource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.Map;

public abstract class RoutingDataSource extends AbstractRoutingDataSource
        implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public Map<Object, Object> getTargetSource() {
        try {
            Field field = AbstractRoutingDataSource.class.getDeclaredField("targetDataSources");
            field.setAccessible(true);
            return (Map<Object, Object>) field.get(this);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Map<Object, DataSource> getResolvedDataSources() {
        try {
            Field field = AbstractRoutingDataSource.class.getDeclaredField("resolvedDataSources");
            field.setAccessible(true);
            return (Map<Object, DataSource>) field.get(this);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}

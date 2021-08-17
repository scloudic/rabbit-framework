package com.scloudic.rabbitframework.jbatis.springboot.test;

import com.scloudic.rabbitframework.jbatis.dataaccess.datasource.RoutingDataSource;

public class RoutingDataSourceTest extends RoutingDataSource {
    /**
     * Determine the current lookup key. This will typically be
     * implemented to check a thread-bound transaction context.
     * <p>Allows for arbitrary keys. The returned key needs
     * to match the stored lookup key type, as resolved by the
     * {@link #resolveSpecifiedLookupKey} method.
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return "datasource11";
    }
}

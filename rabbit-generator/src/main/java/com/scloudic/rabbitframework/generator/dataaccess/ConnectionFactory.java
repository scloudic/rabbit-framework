/*
 *  Copyright 2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.scloudic.rabbitframework.generator.dataaccess;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import com.scloudic.rabbitframework.core.utils.ClassUtils;
import com.scloudic.rabbitframework.core.utils.StringUtils;

/**
 * This class assumes that classes are cached elsewhere for performance reasons,
 * but also to make sure that any native libraries are only loaded one time
 * (avoids the dreaded UnsatisfiedLinkError library loaded in another
 * classloader)
 *
 * @author Jeff Butler
 */
public class ConnectionFactory {

    private static ConnectionFactory instance = new ConnectionFactory();

    public static ConnectionFactory getInstance() {
        return instance;
    }

    /**
     *
     */
    private ConnectionFactory() {
        super();
    }

    public Connection getConnection(JdbcConnectionInfo config)
            throws SQLException {
        Driver driver = getDriver(config);

        Properties props = new Properties();

        if (StringUtils.isNotEmpty(config.getUserName())) {
            props.setProperty("user", config.getUserName());
        }

        if (StringUtils.isNotEmpty(config.getPassword())) {
            props.setProperty("password", config.getPassword());
        }
        props.putAll(config.getProperties());
        Connection conn = driver.connect(config.getConnectionURL(), props);

        if (conn == null) {
            throw new SQLException("Cannot connect to database (possibly bad driver/URL combination)");
        }

        return conn;
    }

    private Driver getDriver(JdbcConnectionInfo connectionInformation) {
        String driverClass = connectionInformation.getDriverClass();
        Driver driver;
        try {
            driver = (Driver) ClassUtils.newInstance(driverClass);
        } catch (Exception e) {
            throw new RuntimeException("Exception getting JDBC Driver"); //$NON-NLS-1$
        }

        return driver;
    }
}

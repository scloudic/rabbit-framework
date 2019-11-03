/**
 * Copyright 2009-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rabbitframework.jade;

import com.rabbitframework.jade.builder.Configuration;
import com.rabbitframework.jade.dataaccess.DefaultSqlDataAccess;
import com.rabbitframework.jade.dataaccess.SqlDataAccess;

public class DefaultRabbitJadeFactory implements RabbitJadeFactory {
    private Configuration configuration;

    public DefaultRabbitJadeFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlDataAccess openSqlDataAccess() {
        return new DefaultSqlDataAccess(configuration);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

}

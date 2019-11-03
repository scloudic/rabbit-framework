/**
 *    Copyright 2009-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.rabbitframework.jade;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import com.rabbitframework.jade.builder.Configuration;
import com.rabbitframework.jade.builder.XMLConfigBuilder;
import com.rabbitframework.jade.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link RabbitJadeFactory}
 */
public class RabbitJadeFactoryBuilder {
    private static final Logger logger = LoggerFactory.getLogger(RabbitJadeFactoryBuilder.class);

    public RabbitJadeFactory build(Reader reader) {
        return build(reader, null);
    }

    public RabbitJadeFactory build(Reader reader, Properties properties) {
        try {
            XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader,
                    properties);
            return build(xmlConfigBuilder.parse());
        } catch (Exception e) {
            String msg = "Error building SqlSession.";
            logger.error(msg, e);
            throw new PersistenceException(msg, e);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    public RabbitJadeFactory build(InputStream inputStream) {
        return build(inputStream, null);
    }

    public RabbitJadeFactory build(InputStream inputStream,
                                   Properties properties) {
        try {
            XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(
                    inputStream, properties);
            return build(xmlConfigBuilder.parse());
        } catch (Exception e) {
            String msg = "Error building SqlSession.";
            logger.error(msg, e);
            throw new PersistenceException(msg, e);
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }
    }

    public RabbitJadeFactory build(Configuration configuration) {
        return new DefaultRabbitJadeFactory(configuration);
    }
}
